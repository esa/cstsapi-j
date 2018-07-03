package esa.egos.proxy.tml;

import java.io.IOException;
import java.io.InputStream;

import esa.egos.csts.api.exceptions.ApiException;


public interface ITMLMessageFactory
{
    TMLMessage createTmlMessage(byte[] initialEightBytes, InputStream is) throws ApiException, IOException;
}
