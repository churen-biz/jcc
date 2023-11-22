
#### running code in the ubuntu docker container
```
docker container run -itd \
 --name jcc \
 -v /Users/lihai/IdeaProjects/self/j/jcc:/root/jcc:z \
 --privileged=true ubuntu:20.04
```
#### touch into the container 
- `docker exec -ti jcc bash`