package org.fuelteam.watt.lucky.timeing;

import java.util.*;
import java.util.function.Consumer;

/**
 * The sequence of items in a collection before a predetermined timeout between items has been reached.
 */
class SessionTimeWindow<T> implements TimeWindow<T> {
    long _startTime;
    long _endTime;
    long _timeout;
    List<T> _items;
    int _size;

    public SessionTimeWindow(long timeout) {
        _timeout = timeout;
        _startTime = 0;
        _endTime = 0;
    }

    /**
     * Set the items used in this SessionWindow.
     * @param source the source list to iterate over.
     * @param startIndex the index in the source list to start at
     * @param selector the selector used to pull a timestamp out of an object
     * @return the last touched index
     */
    int setItems(List<T> source, int startIndex, TimestampSelector<T> selector) {
        LinkedList<T> list = new LinkedList<T>();
        long prev = 0, cur = 0;
        boolean first = true;
        int index;
        // start at the last used index, loop until the timeout between items is reached
        for (index = startIndex; index < source.size(); index++) {
            T itemCur = source.get(index);
            cur = selector.select(itemCur);
            if (first) {
                prev = cur;
                first = false;
                // set the start time of this window
                _startTime = prev;
                list.add(itemCur);
                continue;
            }

            if (cur - prev > _timeout) {
                // set the end time of this window
                _endTime = prev;
                break;
            } else {
                prev = cur;
                list.add(itemCur);
                _size++;
            }
            _endTime = cur;
        }

        _items = list;
        return index;
    }

    @Override
    public int size() {
        if (_items != null)
            return _items.size();
        else
            return 0;
    }

    @Override
    public Iterator<T> iterator() {
        if (_items == null)
            return Collections.emptyIterator();
        else {
            return _items.iterator();
        }
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        if (_items != null && _items.size() > 0) {
            _items.forEach(action);
        }
    }

    @Override
    public Spliterator<T> spliterator() {
        if (_items == null) {
            return Spliterators.emptySpliterator();
        } else {
            return _items.spliterator();
        }
    }

    @Override
    public long getStartTime() {
        return _startTime;
    }

    @Override
    public long getEndTime() {
        return _endTime;
    }
}