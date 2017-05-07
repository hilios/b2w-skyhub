# SkyHub challenge

```shell
$ docker run -p 27017:27017 -d mongo -vvv --noauth --bind_ip 0.0.0.0
```

#### Resize photos

Your mission:
- Consume a webservice endpoint (http://54.152.221.29/images.json) that returns a JSON of photos. There are 10 photos.
- Generate three different formats for each photo. The dimensions are: small (320x240), medium (384x288) and large (640x480).
- Write a webservice endpoint that lists (in JSON format) all the ten photos with their respective formats, providing their URLs.

Choose the programming language you want... let us know about why is that your choice. Besides the solution itself, write an automated test for your code (using a known framework or just another function/method).

You should use a non-relational database (MongoDB is preferred).

Your code should be delivered in your Github account.

We are interested in your solution considering:
1. Correctness;
2. Readability;
3. Automated tests;
4. Execution time.