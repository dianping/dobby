package com.dianping.dobby.book.biz;


public enum BorrowState {

	RETURNED(1,2),
	BORROWING(2,1,2);

	private int id;
	private int[] nextIds;

	BorrowState(int id, int... nextIds){
		this.id = id;
		this.nextIds = nextIds;
	}

	public int getId(){
		return id;
	}

	private void check(BorrowState nextState){
		if (nextState == null) {
			return;
		}

		int nextId = nextState.getId();
		for(int id: nextIds){
			if(id == nextId)
				return;
		}

		String message = String.format("Internal erorr: can't move ticket state from %s to %s!", this, nextState);

		throw new IllegalStateException(message);
	}

	public void moveTo(DefaultBorrowContext ctx, BorrowState nextState) throws Exception{		
		check(nextState);

		BorrowListener listener = ctx.getListener();

		ctx.setState(this);
		listener.beforeStateChange(ctx);
		ctx.setState(nextState);
		listener.afterStateChange(ctx);
	}

	public static BorrowState getByName(String name, BorrowState defaultState) {
		for (BorrowState state : values()) {
			if (state.name().equals(name)) {
				return state;
			}
		}
		return defaultState;
	}

}
