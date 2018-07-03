package esa.egos.csts.api.util.impl;

import java.util.ArrayList;

import esa.egos.csts.api.util.ObservableList;

public class ObservableArrayList<E> extends ObservableList<E> {

	public ObservableArrayList() {
		super(new ArrayList<>());
	}

}
