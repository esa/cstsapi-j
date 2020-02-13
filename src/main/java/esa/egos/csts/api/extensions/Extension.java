package esa.egos.csts.api.extensions;

import java.util.NoSuchElementException;
import java.util.Optional;

import com.beanit.jasn1.ber.types.BerNull;

import b1.ccsds.csts.common.types.Extended;

/**
 * This class represents the CCSDS Extended type.
 */
public class Extension {

	private Optional<EmbeddedData> embeddedData;

	private Extension() {
		embeddedData = Optional.empty();
	}

	/**
	 * Creates an unused Extension.
	 * 
	 * @return an unused Extension
	 */
	public static Extension notUsed() {
		return new Extension();
	}

	/**
	 * Creates an Extension with the specified embedded data.
	 * 
	 * @param embeddedData
	 *            the specified embedded data
	 * @return an Extension with the specified embedded data
	 */
	public static Extension of(EmbeddedData embeddedData) {
		Extension extension = new Extension();
		extension.setEmbedded(embeddedData);
		return extension;
	}

	/**
	 * Returns if the extension is used.
	 * 
	 * @return true if the extension is used, false otherwise
	 */
	public boolean isUsed() {
		return embeddedData.isPresent();
	}

	/**
	 * Returns the embedded data.
	 * 
	 * @return the embedded data
	 * @throws NoSuchElementException
	 *             if the extension is not used
	 */
	public EmbeddedData getEmbeddedData() {
		return embeddedData.get();
	}

	private void setEmbedded(EmbeddedData embedded) {
		embeddedData = Optional.of(embedded);
	}

	/**
	 * Encodes this Extension into a CCSDS Extended type.
	 * 
	 * @return the CCSDS Extended type representing this object
	 */
	public Extended encode() {
		Extended extended = new Extended();
		if (embeddedData.isPresent()) {
			extended.setExternal(embeddedData.get().encode());
		} else {
			extended.setNotUsed(new BerNull());
		}
		return extended;
	}

	/**
	 * Decodes a specified CCSDS Extended type.
	 * 
	 * @param extended
	 *            the specified CCSDS Extended type
	 * @return a new Extension decoded from the specified CCSDS Extended type
	 */
	public static Extension decode(Extended extended) {
		Extension extension = new Extension();
		if (extended.getExternal() != null) {
			extension.setEmbedded(EmbeddedData.decode(extended.getExternal()));
		}
		return extension;
	}

	@Override
	public String toString() {
		return "Extension [embeddedData=" + embeddedData + "]";
	}

}
