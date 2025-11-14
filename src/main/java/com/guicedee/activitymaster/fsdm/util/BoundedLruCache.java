package com.guicedee.activitymaster.fsdm.util;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A lightweight, dependency-free, access-order LRU cache with a fixed maximum number of entries.
 *
 * - Thread-safe via synchronized map wrapper.
 * - O(1) get/put operations in practice.
 * - Evicts the least-recently-accessed entry when the maximum size is exceeded.
 *
 * Note: This cache bounds the number of entries, not exact memory usage. Use an
 * approximate per-entry size to derive an entry cap for a given memory budget.
 */
public final class BoundedLruCache<K, V>
{
	private final int maxEntries;
	private final Map<K, V> delegate;

	public BoundedLruCache(int maxEntries)
	{
		if (maxEntries <= 0)
		{
			throw new IllegalArgumentException("maxEntries must be > 0");
		}
		this.maxEntries = maxEntries;
		this.delegate = Collections.synchronizedMap(new LinkedHashMap<K, V>(16, 0.75f, true)
		{
			@Override
			protected boolean removeEldestEntry(Map.Entry<K, V> eldest)
			{
				return size() > BoundedLruCache.this.maxEntries;
			}
		});
	}

	public V get(K key)
	{
		return delegate.get(key);
	}

	public void put(K key, V value)
	{
		Objects.requireNonNull(key, "key");
		delegate.put(key, value);
	}

	public V remove(K key)
	{
		return delegate.remove(key);
	}

	public int size()
	{
		return delegate.size();
	}

	public void clear()
	{
		delegate.clear();
	}
}
