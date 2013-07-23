package yaps.local_simulator;

public enum ActionType {
	GO_TO,			//param1: node
	GO_THROUGH,     //param1: edge
	BROADCAST_MSG,	//param1: the message
	PRIVATE_MSG, 	//param1: destiny | param2: the message
	MARK_NODE,		//param1: the message
	MARK_EDGE		//param1: the message
}