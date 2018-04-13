package esa.egos.csts.api.proxy.tml;

import java.io.IOException;
import java.io.InputStream;

import esa.egos.csts.api.exception.ApiException;


public interface ITMLMessageFactory
{
    TMLMessage createTmlMessage(byte[] initialEightBytes, InputStream is) throws ApiException, IOException;
}
