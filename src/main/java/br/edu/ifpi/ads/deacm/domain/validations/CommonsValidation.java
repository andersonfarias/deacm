package br.edu.ifpi.ads.deacm.domain.validations;

public enum CommonsValidation implements Validation {

	REQUIRED_FIELD {

		@Override
		public void validate( Object... args ) throws RuntimeException {
			Object notNullObject = args[0];
			if ( notNullObject == null )
				throw new RuntimeException( String.format( "It's required to inform the ''.", args[1] ) );
		}

	},

	GREATHER_THAN_ZERO {

		@Override
		public void validate( Object... args ) throws RuntimeException {
			Object number = args[0];
			if ( number == null )
				return;

			if ( number instanceof Integer ) {
				Integer i = (Integer) number;
				if ( i <= 0 )
					throw new RuntimeException( String.format( "The '%s' must be a number greather than zero.", args[1] ) );
				return;
			}

			if ( number instanceof Long ) {
				Long i = (Long) number;
				if ( i <= 0 )
					throw new RuntimeException( String.format( "The '%s' must be a number greather than zero.", args[1] ) );
				return;
			}

			if ( number instanceof Float ) {
				Float i = (Float) number;
				if ( i <= 0 )
					throw new RuntimeException( String.format( "The '%s' must be a number greather than zero.", args[1] ) );
				return;
			}

			if ( number instanceof Double ) {
				Double i = (Double) number;
				if ( i <= 0 )
					throw new RuntimeException( String.format( "The '%s' must be a number greather than zero.", args[1] ) );
				return;
			}

			throw new RuntimeException( String.format( "The argument '%s' must be a number.", args[1] ) );
		}

	};

	@Override
	public abstract void validate( Object... args ) throws RuntimeException;
}