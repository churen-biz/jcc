#!/usr/bin/env bash

mvn clean compile assembly:single
mv ./target/jcc-0.0.1-SNAPSHOT-jar-with-dependencies.jar ./target/jcc.jar
cd ./target || exit

assert() {
  expected="$1"
  input="$2"

  java -jar jcc.jar "$input" > tmp.s
  gcc -static -o tmp tmp.s
  ./tmp
  actual="$?"

  if [ "$actual" = "$expected" ]; then
    echo "$input => $actual"
  else
    echo "$input => $expected expected, but got $actual"
    exit 1
  fi
}

assert 0 0
assert 42 42
assert 42 "40+2"
assert 2 "7-5"
assert 75 " 77  -5 +  3  "
assert 21 '5+20-4'
assert 41 ' 12 + 34 - 5 '
assert 47 '5+6*7'
assert 15 '5*(9-6)'
assert 4 '(3+5)/2'

echo OK