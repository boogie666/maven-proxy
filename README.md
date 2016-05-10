# maven-repo [![Build Status](https://travis-ci.org/boogie666/maven-proxy.svg?branch=master)](https://travis-ci.org/boogie666/maven-proxy)

A simple maven proxy

## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

## Running

To start a web server for the application, run:

    lein ring server-headless (no point with starting a browser)

## Building

To build the cli app run:

    lein ring uberjar

## Download the latest release

You can get the latest build [here][]

[here]: https://github.com/boogie666/maven-proxy/releases/latest


## Running the proxy

First of all you'll need to generate the default config file like so:
    
    java -jar maven-proxy.jar --generate-config

This will generate a file named maven-repo-config.edn
Alternatively you can specify a file name like so:
    
    java -jar maven-proxy.jar --generate-config config.edn
    
This will generate a file named config.edn with the default configs.

Once you have the default config setup you can run maven-proxy like so:

    java -jar maven-proxy.jar --config-file <name of config file>

# The Config File

The generated config file will look like this:
    
    { :port 1337 :endpoint "/maven2" :repo-location "/maven-repo" :proxies ["https://repo.maven.apache.org/maven2"] :users [] }

The configs:
    
    :port <number>          - specify what port the repo should start on (default is 1337)
    :endpoint <string>      - where the users will connect to (default is /maven2), so if the proxy is running on http://localhost:1337
                              users will have to config maven to go to http://localhost:1337/maven2
    :repo-location <string> - where, on the hard-drive of the server, is the repo stored.
    :proxies <vector>       - a list of maven repos to be proxied
    :users <vector          - a list of user-password pairs used for basic auth.
                              so if you add :users [{"admin" "admin_pass"}] users can connect using 'admin' as the user and 'admin_pass' as the password. 
                              you can add multiple user-password entries.
## License

MIT
