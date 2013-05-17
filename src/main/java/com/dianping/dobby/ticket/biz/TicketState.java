package com.dianping.dobby.ticket.biz;

public enum TicketState {
	CREATED(1, 2, 9),

	ASSIGNED(2, 3, 4, 9),

	ACCEPTED(3, 2, 4, 9),

	RESOLVED(4, 2, 4, 9),

	IGNORED(9);

	public static TicketState getByName(String name, TicketState defaultState) {
		for (TicketState state : values()) {
			if (state.name().equals(name)) {
				return state;
			}
		}

		return defaultState;
	}

	private int m_id;

	private int[] m_nextIds;

	private TicketState(int id, int... nextIds) {
		m_id = id;
		m_nextIds = nextIds;
	}

	private void check(TicketState nextState) {
		if (nextState == null || nextState == this) {
			return;
		}

		int nextId = nextState.getId();

		for (int id : m_nextIds) {
			if (id == nextId) {
				return;
			}
		}

		String message = String.format("Internal erorr: can't move ticket state from %s to %s!", this, nextState);

		throw new IllegalStateException(message);
	}

	public int getId() {
		return m_id;
	}

	public void moveTo(DefaultTicketContext ctx, TicketState nextState) throws Exception {
		if (nextState == null) {
			nextState = this;
		}

		check(nextState);

		TicketListener listener = ctx.getListener();

		ctx.setState(this);
		ctx.setNextState(nextState);
		listener.beforeStateChange(ctx);
		ctx.setState(nextState);
		ctx.setNextState(null);
		listener.afterStateChange(ctx);
	}
}
