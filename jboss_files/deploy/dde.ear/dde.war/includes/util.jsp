<%@ page import="java.io.IOException,
                 java.util.GregorianCalendar,
                 java.io.FileWriter,
                 java.util.Vector,
                 java.util.Hashtable"%>
<%

	class DebugMessages {
		// HASHTABLE OF VECTORS
		Hashtable hashMessages = new Hashtable();
		Vector empty = new Vector();

		public DebugMessages () {}

		public Vector getMsgs(String source) {
			Object objVec = hashMessages.get(source);
			if (objVec == null || !(objVec instanceof Vector)) {
				return empty;
			} else {
				return (Vector)objVec;
			}
		}

		public void addMsg(String source, String msg) {
			Object objVec = hashMessages.get(source);
			if (objVec == null) {
				objVec = new Vector();
				hashMessages.put(source, objVec);
			}
			((Vector)objVec).addElement(msg);
			try {
				//File file = new File("debug.out");
				FileWriter writer = new FileWriter("debug.out", true);
				writer.write(new GregorianCalendar().getTime() + ": " + source + ": '" + msg + "'\r\n");
				writer.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}

	}

	DebugMessages debug = new DebugMessages();

%>