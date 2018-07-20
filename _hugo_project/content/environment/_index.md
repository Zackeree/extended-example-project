+++
title = "Development Environment"
chapter = true
pre = "<b>1. </b>"
+++
# Development Environment

{{% notice info %}}
This is an info callout.
{{% /notice %}}

## This is a Subsection
This is some informational text. Take a look at the following code snippet.

```
<template>
  <h1>Hello {{ name }}</h1>
</template>

<script>
  export default {
    name: 'MyFirstVueComponent',
    data() {
      return {
        name: 'World!'
      }
    }
  };
</script>
```