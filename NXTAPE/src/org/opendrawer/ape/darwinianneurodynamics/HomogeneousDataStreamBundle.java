package org.opendrawer.ape.darwinianneurodynamics;

public class HomogeneousDataStreamBundle extends DataStreamBundle {

	private DataProvider dataProvider;

	public HomogeneousDataStreamBundle(DataProvider dataProvider, int dataWidth) {
		super(dataWidth);
		this.dataProvider = dataProvider;
		this.dataWidth = dataWidth;
		int channels = dataProvider.getChannelCount();
		for (int i = 0; i < channels; i++)
			dataStreams.add(new DataStream(dataProvider, i, dataWidth));
	}

	public DataProvider getDataProvider() {
		return dataProvider;
	}

}
