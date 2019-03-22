package org.fuelteam.watt.lucky.timeing;

import java.util.*;
import java.util.function.Consumer;

/**
 * The TumblingWindowCollection transforms a collection into an iterable collection of sequential time windows. This
 * wrapper class can be used to manage the retention policy and add objects in chronological order to the underlying
 * source collection.
 */
public class TumblingWindowCollection<T> implements Iterable<TimeWindow<T>> {
    List<T> _source;
    private TimestampSelector<T> _selector;
    long _startTime;
    long _windowDuration;

    public TumblingWindowCollection(List<T> source, TimestampSelector<T> selector, long windowDuration, long startTime) {
        init(source, selector, windowDuration, startTime);
    }

    private void init(List<T> source, TimestampSelector<T> selector, long windowDuration, long startTime) {
        _source = source;
        _selector = selector;
        _windowDuration = windowDuration;
        _startTime = startTime;

        performEviction();
    }

    public void add(T item) {
        if (_source.size() == 0)
            _source.add(0, item);
        else
            Utils.addTimeOrdered(_source, _selector, item);

        performEviction();
    }

    private void performEviction() {
        Utils.performEviction(_source, _selector, _startTime);
    }

    @Override
    public Iterator<TimeWindow<T>> iterator() {
        if (_source == null || _source.size() == 0) {
            return Collections.emptyIterator();
        } else {
            long end = _selector.select(_source.get(_source.size() - 1)) + 1;
            return Windowing.toTumblingWindows(_source, _selector, _startTime, end, _windowDuration).iterator();
        }
    }

    @Override
    public void forEach(Consumer<? super TimeWindow<T>> action) {
        if (_source != null && _source.size() != 0) {
            long end = _selector.select(_source.get(_source.size() - 1)) + 1;
            Windowing.toTumblingWindows(_source, _selector, _startTime, end, _windowDuration).forEach(action);
        }
    }

    @Override
    public Spliterator<TimeWindow<T>> spliterator() {
        if (_source == null || _source.size() == 0) {
            return Spliterators.emptySpliterator();
        } else {
            long end = _selector.select(_source.get(_source.size() - 1)) + 1;
            return Windowing.toTumblingWindows(_source, _selector, _startTime, end, _windowDuration).spliterator();
        }
    }
}
