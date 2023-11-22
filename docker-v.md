docker container run -itd \
 --name jcc \
 -v /Users/lihai/IdeaProjects/self/j/jcc:/root/jcc:z \
 --privileged=true ubuntu:20.04

### docker exec -ti jcc bash