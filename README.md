A simple command line utility to turn UTF-8 input into a 64-bit hash, wrapped in a docker container.


# Run

$ cd your_repo

$ docker build -t test .

$ cat your_test.txt | docker run -i test 


# To-Do

Pass in custom parameters as arguments to docker run command(--window/--thread)
