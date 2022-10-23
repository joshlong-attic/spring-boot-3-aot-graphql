#!/usr/bin/env bash
mvn -Pnative -DskipTests clean native:compile &&  ./target/graphql
