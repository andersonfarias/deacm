package br.edu.ifpi.ads.deacm.domain.lp;

import org.apache.commons.csv.CSVRecord;

import br.edu.ifpi.ads.deacm.web.rest.dto.UploadFileDTO;

public enum Models {

	MAXIMIZATION_OF_EFFICIENCY_OF_A_SINGLE_DMU( "1", "maximization of efficiency of a single DMU" ) {

		@Override
		public void collectData( UploadFileDTO uploadDTO, CSVRecord metaData ) {

			if ( metaData.size() != 2 )
				throw new RuntimeException( String.format( "It's expected 2 parameters for the model '%s'", getDescription() ) );

			String capitalString = metaData.get( 0 );
			if ( capitalString == null || capitalString.isEmpty() )
				throw new RuntimeException( "It's required to give the capital" );

			Double capital = 0d;
			try {
				capital = Double.valueOf( capitalString );
			} catch ( NumberFormatException e ) {
				throw new RuntimeException( "The given capital is not a valid double" );
			}

			String ksString = metaData.get( 1 );
			if ( ksString == null || ksString.isEmpty() )
				throw new RuntimeException( "It's required to give the coefficient of size" );

			Double ks = 0d;
			try {
				ks = Double.valueOf( ksString );
			} catch ( NumberFormatException e ) {
				throw new RuntimeException( "The given coefficient of size is not a valid double" );
			}

			uploadDTO.setCapital( capital );
			uploadDTO.setKs( ks );
		}

	},

	MAXIMIZATION_OF_SIZE_OF_A_SINGLE_DMU( "2", "maximization of size of a single DMU" ) {

		@Override
		public void collectData( UploadFileDTO uploadDTO, CSVRecord metaData ) {
			if ( metaData.size() != 2 )
				throw new RuntimeException( String.format( "It's expected 2 parameters for the model '%s'", getDescription() ) );

			String capitalString = metaData.get( 0 );
			if ( capitalString == null || capitalString.isEmpty() )
				throw new RuntimeException( "It's required to give the capital" );

			Double capital = 0d;
			try {
				capital = Double.valueOf( capitalString );
			} catch ( NumberFormatException e ) {
				throw new RuntimeException( "The given capital is not a valid double" );
			}

			String keString = metaData.get( 1 );
			if ( keString == null || keString.isEmpty() )
				throw new RuntimeException( "It's required to give the coefficient of efficiency" );

			Double ke = 0d;
			try {
				ke = Double.valueOf( keString );
			} catch ( NumberFormatException e ) {
				throw new RuntimeException( "The given coefficient of efficiency is not a valid double" );
			}

			uploadDTO.setCapital( capital );
			uploadDTO.setKe( ke );
		}

	},

	MINIMIZATION_OF_CAPITAL_OF_A_SINGLE_DMU( "3", "minimization of capital of a single DMU" ) {

		@Override
		public void collectData( UploadFileDTO uploadDTO, CSVRecord metaData ) {
			String ksString = metaData.get( 0 );
			if ( ksString == null || ksString.isEmpty() )
				throw new RuntimeException( "It's required to give the coefficient of size" );

			Double ks = 0d;
			try {
				ks = Double.valueOf( ksString );
			} catch ( NumberFormatException e ) {
				throw new RuntimeException( "The given coefficient of size is not a valid double" );
			}

			String keString = metaData.get( 1 );
			if ( keString == null || keString.isEmpty() )
				throw new RuntimeException( "It's required to give the coefficient of efficiency" );

			Double ke = 0d;
			try {
				ke = Double.valueOf( keString );
			} catch ( NumberFormatException e ) {
				throw new RuntimeException( "The given coefficient of efficiency is not a valid double" );
			}

			uploadDTO.setKs( ks );
			uploadDTO.setKe( ke );
		}

	},

	MINIMIZATION_OF_CAPITAL_OF_A_SUBSET_OF_DMUS( "4", "minimization of capital of a subset of DMUs" ) {

		@Override
		public void collectData( UploadFileDTO uploadDTO, CSVRecord metaData ) {
			if ( metaData.size() != 1 )
				throw new RuntimeException( String.format( "It's expected 1 parameters for the model '%s'", getDescription() ) );

			String tdString = metaData.get( 0 );
			if ( tdString == null || tdString.isEmpty() )
				throw new RuntimeException( "It's required to give the coefficient of distance between the efficiencies vector" );

			Double td = 0d;
			try {
				td = Double.valueOf( tdString );
			} catch ( NumberFormatException e ) {
				throw new RuntimeException( "The given coefficient of distance is not a valid double" );
			}

			uploadDTO.setTd( td );
		}

	},

	MAXIMIZATION_OF_EFFICIENCY_OF_A_SUBSET_OF_DMUS( "5", "maximization of efficiency of a subset of DMUs" ) {

		@Override
		public void collectData( UploadFileDTO uploadDTO, CSVRecord metaData ) {
			if ( metaData.size() != 1 )
				throw new RuntimeException( String.format( "It's expected 1 parameters for the model '%s'", getDescription() ) );

			String capitalString = metaData.get( 0 );
			if ( capitalString == null || capitalString.isEmpty() )
				throw new RuntimeException( "It's required to give the capital" );

			Double capital = 0d;
			try {
				capital = Double.valueOf( capitalString );
			} catch ( NumberFormatException e ) {
				throw new RuntimeException( "The given capital is not a valid double" );
			}

			uploadDTO.setCapital( capital );
		}

	};

	private final String code;

	private final String description;

	public abstract void collectData( UploadFileDTO uploadDTO, CSVRecord metaData );

	private Models( String code, String description ) {
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

}