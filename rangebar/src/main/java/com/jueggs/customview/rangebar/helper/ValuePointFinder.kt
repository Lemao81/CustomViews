package com.jueggs.customview.rangebar.helper

class ValuePointFinder {
    fun findValuePoint(backwardIterator: ListIterator<ValuePoint>, forwardIterator: ListIterator<ValuePoint>, position: Float): ValuePoint? {
        return when {
            !backwardIterator.hasPrevious() && forwardIterator.hasNext() -> searchForward(forwardIterator, position)
            !forwardIterator.hasNext() && backwardIterator.hasPrevious() -> searchBackward(backwardIterator, position)
            backwardIterator.hasPrevious() && forwardIterator.hasNext() -> searchBothDirections(backwardIterator, forwardIterator, position)
            else -> null
        }
    }

    private fun searchForward(iterator: ListIterator<ValuePoint>, position: Float): ValuePoint? {
        val next = iterator.next()

        return when {
            next.contains(position) -> next
            iterator.hasNext() -> searchForward(iterator, position)
            else -> null
        }
    }

    private fun searchBackward(iterator: ListIterator<ValuePoint>, position: Float): ValuePoint? {
        val prev = iterator.previous()

        return when {
            prev.contains(position) -> prev
            iterator.hasPrevious() -> searchBackward(iterator, position)
            else -> null
        }
    }

    private fun searchBothDirections(backwardIterator: ListIterator<ValuePoint>, forwardIterator: ListIterator<ValuePoint>, position: Float): ValuePoint? {
        val prev = backwardIterator.previous()
        val next = forwardIterator.next()

        return when {
            prev.contains(position) -> prev
            next.contains(position) -> next
            else -> findValuePoint(backwardIterator, forwardIterator, position)
        }
    }
}