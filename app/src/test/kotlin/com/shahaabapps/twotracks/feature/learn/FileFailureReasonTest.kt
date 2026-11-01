package com.shahaabapps.twotracks.feature.learn

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

/**
 * What a failed upload, download or removal says.
 *
 * The point of these is that the two likely failures ask for opposite things
 * from the person reading: a dropped connection is worth another press, and a
 * space that is not allowed to keep files will never work on the second try.
 * A message that says neither sends someone looking in the wrong place, which
 * is exactly what happened the first time a file was added.
 */
class FileFailureReasonTest {

    @Test
    fun `a storage rule that forbids the write says so, not that it broke`() {
        // The wording Cloud Storage uses when the rules deny a write.
        val denied = IllegalStateException("User does not have permission to access this object.")
        assertEquals("your space is not allowed to keep files yet", reason(denied))
    }

    @Test
    fun `permission denied is recognised however it is worded`() {
        listOf("Permission denied on resource", "The caller is not authorized", "PERMISSION_DENIED").forEach {
            assertEquals("for \"$it\"", "your space is not allowed to keep files yet", reason(IllegalStateException(it)))
        }
    }

    @Test
    fun `a dropped connection reads as one, and invites another try`() {
        assertEquals("the connection dropped", reason(IOException("Software caused connection abort")))
        assertEquals("the connection dropped", reason(IllegalStateException("The operation retry limit has been exceeded.")))
    }

    @Test
    fun `the cause is read as well as the exception itself`() {
        // Cloud Storage wraps the useful sentence one layer down more often
        // than not, and the top-level message is "An unknown error occurred".
        val wrapped = IllegalStateException(
            "An unknown error occurred",
            IllegalStateException("User does not have permission to access this object."),
        )
        assertEquals("your space is not allowed to keep files yet", reason(wrapped))
    }

    @Test
    fun `anything else is passed through rather than swallowed`() {
        assertEquals("Bucket two-tracks does not exist", reason(IllegalStateException("Bucket two-tracks does not exist")))
        // Nothing to say is still said: a blank line under a failed upload
        // reads as the upload having worked.
        assertTrue(reason(IllegalStateException()).isNotBlank())
    }

    @Test
    fun `a long message is cut rather than filling the screen`() {
        val long = reason(IllegalStateException("x".repeat(400)))
        assertTrue("the message is $long characters", long.length <= 90)
    }
}
