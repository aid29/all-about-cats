all-about-cats
===

This is a command line utility for all the people love the cat.

You can fetch some cat's images, cat's categories and cat's facts from Internet with this tools.

## Build
    $ ./build

## Test
    $ ./sbt test

## CDC test
    $ source .run-env
    $ ./sbt "testOnly *CatHttpApiCdcTest"

## Run
    $ ./run

## Todos

Due to timing issue some of below tests are not completed:

- CatImageRepository's integration tests

- Not handling the XML http response parse errors

- High level functional test for the command tools

- Finish all the CDC tests (normally should run them on CI)


