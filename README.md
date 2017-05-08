# SkyHub challenge

A simple API to resize photos written in scala.

**Why Scala?**

Because it is a modern, elegant and very-fast programming language based on the JVM, allowing easy integration with all legacy enterprise solutions writen in Java.

For this challenge Scala really shines. It process each images asynchronously through the actor system, each image is downloaded and resized in parallel and without blocking the request.

## API documentation

| Method | Path | Description |
| --- | --- | --- |
| **GET** | `/ops` | Returns the application version. |
| **GET** | `/images` | Returns all images and its thumbs. |
| **POST** | `/images` | Fetch the images endpoint and generate a thumb to each image that was not processed. |
| **GET** | `/images/:id/:size.jpg` | Returns the thumb for the given image. |

#### Example

```shell
$ curl http://localhost:9000/images
```
Outputs:
```json
{
  "results": [
    {
      "_id": "59107812342ba5c541a0153c",
      "url": "http://54.152.221.29/images/b737_5.jpg",
      "small": "http://localhost:9000/images/59107812342ba5c541a0153c/small.jpg",
      "medium": "http://localhost:9000/images/59107812342ba5c541a0153c/medium.jpg",
      "large": "http://localhost:9000/images/59107812342ba5c541a0153c/large.jpg"
    }
  ]
}
```

## Running

First you need to install the [SBT](http://www.scala-sbt.org/release/docs/Setup.html) and have a [MongoDB](https://docs.mongodb.com/manual/administration/install-community/) instance available.

```shell
$ docker run -p 27017:27017 -d mongo -vvv --noauth --bind_ip 0.0.0.0
```

Then run the server passing the `MONGO_URL` environment variable:

```shell
$  MONGO_URL="mongodb://127.0.0.1" sbt testProd
...

(Starting server. Type Ctrl+D to exit logs, the server will remain in background)

[info] a.e.s.Slf4jLogger - Slf4jLogger started
[info] o.m.d.cluster - Cluster created with settings {hosts=[127.0.0.1:27017], mode=SINGLE, requiredClusterType=UNKNOWN, serverSelectionTimeout='30000 ms', maxWaitQueueSize=500}
[info] play.api.Play - Application started (Prod)
```

This will compile the source code and start the web server at [http://localhost:9000](http://localhost:9000). 

### Tests

You can run the test through the [SBT](http://www.scala-sbt.org/release/docs/Setup.html):

```
$ sbt test
[info] Loading project definition from /b2w-skyhub/project
[info] Set current project to b2w-skyhub (in build file:/b2w-skyhub/)
[info] ApplicationSpec:
[info] OpsController
[info] - should render the version
[info] ErrorHandler
[info] - should send not found on a bad request
[info] ImagesSpec:
[info] GET /images
[info] - should render all images and its thumbs address
[info] POST /images
[info] - should fetch process all images from the endpoint
[info] GET /images/:id/:size.jpg
[info] - should render a image thumb
[info] - should return 404 when image size does not exists
[info] ScalaTest
[info] Run completed in 1 minute.
[info] Total number of tests run: 7
[info] Suites: completed 2, aborted 0
[info] Tests: succeeded 7, failed 0, canceled 0, ignored 0, pending 0
[info] All tests passed.
[info] Passed: Total 7, Failed 0, Errors 0, Passed 7
[success] Total time: 67 s, completed May 8, 2017 1:40:01 PM
```

## Instructions

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