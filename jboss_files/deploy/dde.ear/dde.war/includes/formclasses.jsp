<%@ page import="java.util.Hashtable,
                 java.lang.reflect.Field,
                 java.util.Enumeration"%>
<%
	class Field {
		String fieldLabel;
		String fieldName;
		String fieldValue;
		boolean isRequired;
		String fieldError;

		public Field(String fieldLabelIn, String fieldNameIn, String fieldValueIn, boolean isRequiredIn) {
			fieldLabel = fieldLabelIn;
			fieldName = fieldNameIn;
			fieldValue = fieldValueIn;
			isRequired = isRequiredIn;
			fieldError = "";
		}

		public String getLabel() { return fieldLabel; }
		public String getName() { return fieldName; }
		public String getValue() { return fieldValue; }
		public String getError() { return fieldError; }
		public void setError(String error) { fieldError += error; }

		public boolean isBlank() { return fieldValue.trim().length() == 0; }
		public boolean hasError() { return fieldError.trim().length() > 0; }

		public void doBasicValidation() {
			if (isBlank() && isRequired) {
				setError(fieldLabel + " cannot be left blank.");
			}
		}

		public void getFromRequest(HttpServletRequest req, String defaultIn) {
			fieldValue = req.getParameter(fieldName);
			if (fieldValue == null) {
				fieldValue = defaultIn;
			}
		}
	}

	class Fields {
		Hashtable fields = new Hashtable();

		public Fields() {
		}

		public void add(String hash, Field fIn) {
			fields.put(hash, fIn);
		}

		public Field get(String fieldNameIn) {
			if (fields.get(fieldNameIn) instanceof Field) {
				return (Field)fields.get(fieldNameIn);
			} else {
				return null;
			}
		}

                public Enumeration keys() { return fields.keys(); }
		public Enumeration elements() { return fields.elements(); }
	}
%>