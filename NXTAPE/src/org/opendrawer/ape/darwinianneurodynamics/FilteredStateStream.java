package org.opendrawer.ape.darwinianneurodynamics;

public class FilteredStateStream extends StateStream {
	private final StateStream sourceStream;
	private double dt = Double.NaN;
	private double dtr = Double.NaN;
	private double dv = Double.NaN;
	private double dvr = Double.NaN;

	public FilteredStateStream(StateStream sourceStream) {
		super(sourceStream.getStreamLength());
		this.sourceStream = sourceStream;
		sourceStream.addStreamObserver(this);
	}

	public FilteredStateStream(StateStream sourceStream, double dt, double dtr) {
		this(sourceStream);
		this.dt = dt;
		this.dtr = dtr;
	}

	public FilteredStateStream(StateStream sourceStream, double dt, double dtr,
			double dv, double dvr) {
		this(sourceStream, dt, dtr);
		this.dv = dv;
		this.dvr = dvr;
	}

	@Override
	public void statesUpdated(StatesProvider statesProvider) {
		System.out.println(statesProvider);
		if (dt != Double.NaN && dtr != Double.NaN) {
			double[] sourceStates = sourceStream.read();
			if (dv != Double.NaN && dvr != Double.NaN)
				super.write(Util.distribution(sourceStates, dt, dtr, dv, dvr));
			else
				super.write(Util.distribution(sourceStates, dt, dtr));

		} else
			super.write(sourceStream.read(0));
	}

	@Override
	public void setOutputState(double state, int stateChannel) {
		sourceStream.write(state);
	}
}
