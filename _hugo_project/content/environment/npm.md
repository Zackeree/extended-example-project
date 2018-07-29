---
title: "NPM"
weight: 50
---

NPM (Node Package Manager) is a Javascript package manager. If you're running a modern Linux distro, 
you can likely install it through apt-get: `$ sudo apt-get install npm`. If you are using Windows, go [here](https://www.npmjs.com/package/npm) 
for the download.

### Usage
npm has lots of commands and features. The few you'll use most often are listed below.

`npm install --save package-name` - add package with name `package-name` to `package.json` as a runtime dependency.
`npm install --save-dev package-name` - add package with name `package-name` to `package.json` as a build dependency.