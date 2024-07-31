package androidx.room.util

import androidx.annotation.RestrictTo
import androidx.collection.LongSparseArray

/**
 * Same as [recursiveFetchHashMap] but for [LongSparseArray].
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP_PREFIX)
fun <V> recursiveFetchLongSparseArray(
    map: LongSparseArray<V>,
    isRelationCollection: Boolean,
    fetchBlock: (LongSparseArray<V>) -> Unit
) {
    val tmpMap = LongSparseArray<V>(999)
    var count = 0
    var mapIndex = 0
    val limit = map.size()
    while (mapIndex < limit) {
        if (isRelationCollection) {
            tmpMap.put(map.keyAt(mapIndex), map.valueAt(mapIndex))
        } else {
            // Safe because `V` is a nullable type arg when isRelationCollection == false
            @Suppress("UNCHECKED_CAST")
            tmpMap.put(map.keyAt(mapIndex), null as V)
        }
        mapIndex++
        count++
        if (count == 999) {
            fetchBlock(tmpMap)
            if (!isRelationCollection) {
                map.putAll(tmpMap)
            }
            tmpMap.clear()
            count = 0
        }
    }
    if (count > 0) {
        fetchBlock(tmpMap)
        if (!isRelationCollection) {
            map.putAll(tmpMap)
        }
    }
}