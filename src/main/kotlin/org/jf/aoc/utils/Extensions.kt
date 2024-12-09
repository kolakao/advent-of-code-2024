package org.jf.aoc.utils


fun String.readFile(): String = {}::class.java.classLoader.getResource(this)?.readText().orEmpty()
    .ifEmpty { throw Exception("File '${this}' not found") }


