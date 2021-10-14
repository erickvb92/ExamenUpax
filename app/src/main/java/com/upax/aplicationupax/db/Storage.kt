package com.upax.aplicationupax.db
/**
 * Interface that provides a single way of saving arbitrary data.
 */
interface Storage {
    /**
     * Saves a string
     * @param key the key to identify the data.
     * @param value the value to save
     * @param async flag to indicate if data must be saved in synchronous or asynchronous
     * way, for example, making use of another thread.
     */
    fun saveString(key: String, value: String, async: Boolean = true)

    /**
     * Saves a List concatenating all strings splitting by one pipe |
     * @param key the key to identify the data.
     * @param value the list strings to save
     * @param async flag to indicate if data must be saved in synchronous or asynchronous
     */
    fun saveListString(key: String, value: List<String>, async: Boolean = true)

    /**
     * Saves a integer
     * @param key the key to identify the data.
     * @param value the value to save
     * @param async flag to indicate if data must be saved in synchronous or asynchronous
     * way, for example, making use of another thread.
     */
    fun saveInt(key: String, value: Int, async: Boolean = true)

    /**
     * Saves a float number
     * @param key the key to identify the data.
     * @param value the value to save
     * @param async flag to indicate if data must be saved in synchronous or asynchronous
     * way, for example, making use of another thread.
     */
    fun saveFloat(key: String, value: Float, async: Boolean = true)

    /**
     * Saves a long number
     * @param key the key to identify the data.
     * @param value the value to save
     * @param async flag to indicate if data must be saved in synchronous or asynchronous
     * way, for example, making use of another thread.
     */
    fun saveLong(key: String, value: Long, async: Boolean = true)

    /**
     * Saves a boolean
     * @param key the key to identify the data.
     * @param value the value to save
     * @param async flag to indicate if data must be saved in synchronous or asynchronous
     * way, for example, making use of another thread.
     */
    fun saveBoolean(key: String, value: Boolean, async: Boolean = true)

    /**
     * Saves a Set of Strings
     * @param key the key to identify the data.
     * @param value the value to save
     * @param async flag to indicate if data must be saved in synchronous or asynchronous
     * way, for example, making use of another thread.
     */
    fun saveStringSet(key: String, value: Set<String>, async: Boolean = true)

    /**
     * Returns a string corresponding to [key].
     * @param key The key to retrieve its value. Could be null value.
     * @return string value or null
     */
    fun getString(key: String): String?

    /**
     *  Return a List of strings previously saved linked to [key].
     *  @param key The key to retrieve its value. Could be null value.
     *  @return list of strings value or empty list.
     */
    fun getListString(key: String): List<String>

    /**
     * Returns a Set of string corresponding to [key].
     * @param key The key to retrieve its value. Could be null value.
     * @return Set of string value or null
     */
    fun getStringSet(key: String): Set<String>?

    /**
     * Returns a int corresponding to [key].
     * @param key The key to retrieve its value. Could be null value.
     * @return int value or null
     */
    fun getInt(key: String): Int?

    /**
     * Returns a float corresponding to [key].
     * @param key The key to retrieve its value. Could be null value.
     * @return int value or null
     */
    fun getFloat(key: String): Float?

    /**
     * Returns a long corresponding to [key].
     * @param key The key to retrieve its value. Could be null value.
     * @return long value or null
     */
    fun getLong(key: String): Long?

    /**
     * Returns a boolean corresponding to [key].
     * @param key The key to retrieve its value. Could be null value.
     * @return boolean value or null
     */
    fun getBoolean(key: String): Boolean?

    /**
     * Deletes the element with [key] key.
     * Default implementation does nothing.
     */
    fun delete(key: String) {}

    /**
     * Verifies the element under the [key] exist into storage.
     * @return true if element for such key exist, false otherwise.
     */
    fun validate(key: String): Boolean

    /**
     * Remove an existing key from storage. Default implementation returns null always.
     * @param key The key of the mapping to remove.
     * @return Returns the value that was stored under the key, or null if there
     * was no such key.
     */
    fun remove(key: String): Any? {
        return null
    }

    /**
     * Same as [remove] but makes the cast automatically by replacing the type into
     * the diamond operator. Default implementation returns null always.
     * @param key The key of the mapping to remove.
     * @return Returns the value that was stored under the key, or null if there
     * was no such key.
     */
    fun <T> getAndRemove(key: String): T? {
        return null
    }

    /**
     * Get object as Any but makes the cast automatically by replacing the type into
     * the diamond operator. Default implementation returns null always.
     * @param key The key of the mapping to get.
     * @return Returns the value that was stored under the key, or null if there
     * was no such key.
     */
    fun <T> getAs(key: String): T? {
        return null
    }

    /**
     *  Removes all the elements from storage.
     */
    fun removeAll()
}