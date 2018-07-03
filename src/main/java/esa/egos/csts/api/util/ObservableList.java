package esa.egos.csts.api.util;

import java.util.Collection;
import java.util.List;
import java.util.Observable;
import java.util.stream.Stream;

public abstract class ObservableList<E> extends Observable {

	private List<E> list;

	protected ObservableList(List<E> list) {
		this.list = list;
	}

	public boolean add(E e) {
		boolean ret = list.add(e);
		setChanged();
		notifyObservers();
		return ret;
	}

	public void add(int index, E element) {
		list.add(index, element);
		setChanged();
		notifyObservers();
	}

	public boolean addAll(Collection<? extends E> c) {
		boolean ret = list.addAll(c);
		setChanged();
		notifyObservers();
		return ret;
	}

	public boolean addAll(int index, Collection<? extends E> c) {
		boolean ret = list.addAll(index, c);
		setChanged();
		notifyObservers();
		return ret;
	}

	public void clear() {
		list.clear();
		setChanged();
		notifyObservers();
	}

	public boolean contains(Object o) {
		return list.contains(o);
	}

	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}

	public E get(int index) {
		return list.get(index);
	}

	public int indexOf(Object o) {
		return list.indexOf(o);
	}

	public boolean isEmpty() {
		return list.isEmpty();
	}

	public int lastIndexOf(Object o) {
		return list.lastIndexOf(o);
	}

	public boolean remove(Object o) {
		boolean ret = list.remove(o);
		if (ret) {
			setChanged();
			notifyObservers();
		}
		return ret;
	}

	public E remove(int index) {
		E ret = list.remove(index);
		setChanged();
		notifyObservers();
		return ret;
	}

	public boolean removeAll(Collection<?> c) {
		boolean ret = list.removeAll(c);
		if (ret) {
			setChanged();
			notifyObservers();
		}
		return ret;
	}

	public boolean retainAll(Collection<?> c) {
		boolean ret = list.retainAll(c);
		if (ret) {
			setChanged();
			notifyObservers();
		}
		return ret;
	}

	public E set(int index, E element) {
		E ret = list.set(index, element);
		setChanged();
		notifyObservers();
		return ret;
	}

	public int size() {
		return list.size();
	}

	public Stream<E> stream() {
		return list.stream();
	}

}
