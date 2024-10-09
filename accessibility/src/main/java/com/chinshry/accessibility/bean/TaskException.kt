package com.chinshry.accessibility.bean

import java.io.IOException

open class TaskException(
    message: String? = null,
    throwable: Throwable? = null
) : IOException(message, throwable)

class NodeNotFoundException(
    name: String = ""
) : TaskException("[$name] not found", null)

class NodeClickFailedException(
    name: String = ""
) : TaskException("[$name] click failed", null)

class NodeSetTextFailedException(
    name: String = ""
) : TaskException("[$name] set text found", null)

class UserCancelException(
    throwable: Throwable? = null
) : TaskException(throwable?.message, throwable)