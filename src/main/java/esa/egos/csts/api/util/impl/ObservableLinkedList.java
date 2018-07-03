package esa.egos.csts.api.util.impl;

import java.util.LinkedList;

import esa.egos.csts.api.util.ObservableList;

public class ObservableLinkedList<E> extends ObservableList<E> {

	public ObservableLinkedList() {
		super(new LinkedList<>());
	}

}
