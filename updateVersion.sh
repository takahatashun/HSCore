#!/bin/bash
mvn -N versions:update-child-modules -DgenerateBackupPoms=false
cd ./HSCore-bukkit || return
mvn -N versions:update-child-modules -DgenerateBackupPoms=false
cd ../HSCore-config || return
mvn -N versions:update-child-modules -DgenerateBackupPoms=false
cd ../HSCore-sql || return
mvn -N versions:update-child-modules -DgenerateBackupPoms=false
