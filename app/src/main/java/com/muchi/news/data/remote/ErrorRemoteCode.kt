/*
 * Copyright (c) 2020 - Irfanul Haq.
 */

package com.muchi.news.data.remote

fun handlerErrorResponse(code: Int): String {
    return when (code) {
        400 ->  "Bad Request (400)"
        401 ->  "Unauthorized (401)"
        402 ->  "Payment Required (402)"
        403 ->  "Forbidden (403)"
        404 ->  "Not Found (404)"
        405 ->  "Method Not Allowed (405)"
        406 ->  "Not Acceptable (406)"
        407 ->  "Proxy Authentication Required (407)"
        408 ->  "Request Timeout (408)"
        409 ->  "Conflict (409)"
        410 ->  "Gone (410)"
        411 ->  "Length Required (411)"
        412 ->  "Precondition Failed (412)"
        413 ->  "Payload Too Large (413)"
        414 ->  "Uri Too Long (414)"
        415 ->  "Unsupported MediaType (415)"
        416 ->  "Range Not Satisfiable (416)"
        417 ->  "Expectation Failed (417)"
        418 ->  "I'm A Teapot (418)"
        421 ->  "Misdirected Request (421)"
        422 ->  "Unprocessable Entity (422)"
        423 ->  "Locked (423)"
        424 ->  "Failed Dependency (424)"
        426 ->  "Upgrade Required (426)"
        428 ->  "Precondition Required (428)"
        429 ->  "TooManyRequests (429)"
        431 ->  "Request Header Fields Too Large (431)"
        451 ->  "Unavailable For Legal Reasons (451)"

        500 ->  "Internal Server Error (500)"
        501 ->  "Not Implemented (501)"
        502 ->  "Bad Gateway (502)"
        503 ->  "Service Unavailable (503)"
        504 ->  "Gateway Timeout (504)"
        505 ->  "Http Version Not Supported (505)"
        506 ->  "Variant Also Negates (506)"
        507 ->  "Insufficient Storage (507)"
        508 ->  "Loop Detected (508)"
        510 ->  "Not Extended (510)"
        511 ->  "Network Authentication Required (511)"

        69 ->  "Can't Parse Data Response"
        -1 ->  "No Internet"
        else -> "Unknown Error"
    }
}