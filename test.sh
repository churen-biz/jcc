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

echo OK