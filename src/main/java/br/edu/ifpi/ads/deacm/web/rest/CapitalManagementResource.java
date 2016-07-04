package br.edu.ifpi.ads.deacm.web.rest;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codahale.metrics.annotation.Timed;

import br.edu.ifpi.ads.deacm.domain.DMU;
import br.edu.ifpi.ads.deacm.domain.Input;
import br.edu.ifpi.ads.deacm.domain.Output;
import br.edu.ifpi.ads.deacm.domain.lp.Models;
import br.edu.ifpi.ads.deacm.domain.lp.solutions.KAOSolution;
import br.edu.ifpi.ads.deacm.domain.validations.CommonsValidation;
import br.edu.ifpi.ads.deacm.service.CapitalManagementService;
import br.edu.ifpi.ads.deacm.web.rest.dto.ResponseError;
import br.edu.ifpi.ads.deacm.web.rest.dto.SetDMUDTO;
import br.edu.ifpi.ads.deacm.web.rest.dto.SingleDMUDTO;
import br.edu.ifpi.ads.deacm.web.rest.dto.UploadFileDTO;

@RestController
@RequestMapping( "/api" )
public class CapitalManagementResource {

	private final Logger log = LoggerFactory.getLogger( CapitalManagementResource.class );

	@Inject
	private CapitalManagementService capitalManagementService;

	/**
	 * POST /dea -> Calculate dea.
	 */
	@Timed
	@RequestMapping( value = "/dea", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity<?> dea( @RequestBody List<DMU> dmus ) throws URISyntaxException {
		log.debug( "REST request to dea : {}", dmus );
		if ( dmus == null || dmus.size() < 2 ) {
			return ResponseEntity.badRequest().body( new ResponseError( "It's necessary give at least 2 DMUs for DEA calculation" ) );
		}

		try {
			dmus = capitalManagementService.dea( dmus );
			return new ResponseEntity<>( capitalManagementService.superEficiencyDEA( dmus ), HttpStatus.OK );
		} catch ( RuntimeException e ) {
			return ResponseEntity.badRequest().body( new ResponseError( e.getMessage() ) );
		}
	}

	/**
	 * POST /kao -> Calculate kao.
	 */
	@Timed
	@RequestMapping( value = "/kao", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity<?> kao( @RequestBody List<DMU> dmus ) throws URISyntaxException {
		log.debug( "REST request to kao : {}", dmus );
		if ( dmus == null || dmus.size() < 2 ) {
			return ResponseEntity.badRequest().body( new ResponseError( "It's necessary give at least 2 DMUs for KAO calculation" ) );
		}

		try {
			return new ResponseEntity<>( capitalManagementService.kao( dmus ), HttpStatus.OK );
		} catch ( IOException e ) {
			return ResponseEntity.badRequest().body( new ResponseError( "It wasn possible to execute the KAO's model!\nCause: " + e.getMessage() ) );
		} catch ( RuntimeException e ) {
			return ResponseEntity.badRequest().body( new ResponseError( e.getMessage() ) );
		}
	}

	/**
	 * POST /single/min/capital
	 */
	@Timed
	@RequestMapping( value = "/single/min/capital", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity<?> minCapitalOfSingleDMU( @RequestBody SingleDMUDTO dto ) throws URISyntaxException {
		log.debug( "REST request to single DMU minimization of capital" );

		try {

			CommonsValidation.REQUIRED_FIELD.validate( dto.getTarget(), "target DMU" );
			CommonsValidation.REQUIRED_FIELD.validate( dto.getDmus(), "DMUs" );
			CommonsValidation.REQUIRED_FIELD.validate( dto.getKs(), "coefficient of relative size" );
			CommonsValidation.REQUIRED_FIELD.validate( dto.getKe(), "coefficient of efficiency" );
			CommonsValidation.GREATHER_THAN_ZERO.validate( dto.getKs(), "coefficient of relative size" );
			CommonsValidation.GREATHER_THAN_ZERO.validate( dto.getKe(), "coefficient of efficiency" );

			return new ResponseEntity<>( capitalManagementService.minCapitalOfSingleDMU( dto.getDmus(), dto.getTarget(), dto.getKs(), dto.getKe() ), HttpStatus.OK );
		} catch ( IOException e ) {
			return ResponseEntity.badRequest().body( new ResponseError( "It wasn possible to execute the minimization of capital for single dmu model!\nCause: " + e.getMessage() ) );
		} catch ( RuntimeException e ) {
			return ResponseEntity.badRequest().body( new ResponseError( e.getMessage() ) );
		}
	}

	/**
	 * POST /single/max/size
	 */
	@Timed
	@RequestMapping( value = "/single/max/size", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity<?> maxSizeOfSingleDMU( @RequestBody SingleDMUDTO dto ) throws URISyntaxException {
		log.debug( "REST request to single DMU maximization of relative size" );

		try {

			CommonsValidation.REQUIRED_FIELD.validate( dto.getTarget(), "target DMU" );
			CommonsValidation.REQUIRED_FIELD.validate( dto.getDmus(), "DMUs" );
			CommonsValidation.REQUIRED_FIELD.validate( dto.getCapital(), "capital" );
			CommonsValidation.REQUIRED_FIELD.validate( dto.getKe(), "coefficient of efficiency" );
			CommonsValidation.GREATHER_THAN_ZERO.validate( dto.getKe(), "coefficient of efficiency" );

			return new ResponseEntity<>( capitalManagementService.maxSizeOfSingleDMU( dto.getDmus(), dto.getTarget(), dto.getCapital(), dto.getKe() ), HttpStatus.OK );
		} catch ( IOException e ) {
			return ResponseEntity.badRequest().body( new ResponseError( "It wasn possible to execute the maximization of relative size of single dmu model!\nCause: " + e.getMessage() ) );
		} catch ( RuntimeException e ) {
			return ResponseEntity.badRequest().body( new ResponseError( e.getMessage() ) );
		}
	}

	/**
	 * POST /single/max/efficiency
	 */
	@Timed
	@RequestMapping( value = "/single/max/efficiency", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity<?> maxEfficiencyOfSingleDMU( @RequestBody SingleDMUDTO dto ) throws URISyntaxException {
		log.debug( "REST request to single DMU maximization of efficiency" );

		try {

			CommonsValidation.REQUIRED_FIELD.validate( dto.getTarget(), "target DMU" );
			CommonsValidation.REQUIRED_FIELD.validate( dto.getDmus(), "DMUs" );
			CommonsValidation.REQUIRED_FIELD.validate( dto.getKs(), "coefficient of relative size" );
			CommonsValidation.REQUIRED_FIELD.validate( dto.getCapital(), "capital" );
			CommonsValidation.GREATHER_THAN_ZERO.validate( dto.getKs(), "coefficient of relative size" );

			return new ResponseEntity<>( capitalManagementService.maxEfficiencyOfSingleDMU( dto.getDmus(), dto.getTarget(), dto.getCapital(), dto.getKs() ), HttpStatus.OK );
		} catch ( IOException e ) {
			return ResponseEntity.badRequest().body( new ResponseError( "It wasn possible to execute the maximization of efficiency of single dmu model!\nCause: " + e.getMessage() ) );
		} catch ( RuntimeException e ) {
			return ResponseEntity.badRequest().body( new ResponseError( e.getMessage() ) );
		}
	}

	/**
	 * POST /set/min/capital
	 */
	@Timed
	@RequestMapping( value = "/set/min/capital", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity<?> minCapitalOfDMUSet( @RequestBody SetDMUDTO dto ) throws URISyntaxException {
		log.debug( "REST request to set DMU minimization of capital" );

		try {

			CommonsValidation.REQUIRED_FIELD.validate( dto.getTargets(), "target DMUs" );
			CommonsValidation.REQUIRED_FIELD.validate( dto.getDmus(), "DMUs" );
			CommonsValidation.REQUIRED_FIELD.validate( dto.getKaoDistance(), "Distance between DEA and L2 efficiencies" );
			CommonsValidation.REQUIRED_FIELD.validate( dto.getTd(), "Update Rating of Distance between L2 and DEA efficiencies" );
			CommonsValidation.REQUIRED_FIELD.validate( dto.getMode(), "mode to execute! (Altruistic or Selfish)" );
			CommonsValidation.GREATHER_THAN_ZERO.validate( dto.getTd(), "Update Rating of Distance between L2 and DEA efficiencies" );

			return new ResponseEntity<>( capitalManagementService.minCapitalOfDMUSet( dto.getDmus(), dto.getTargets(), dto.getTd(), dto.getKaoDistance(), dto.getExecuteWithSuperEfficiency(), dto.getMode() ), HttpStatus.OK );
		} catch ( IOException e ) {
			return ResponseEntity.badRequest().body( new ResponseError( "It wasn possible to execute the minimization of capital of a set of dmus model!\nCause: " + e.getMessage() ) );
		} catch ( RuntimeException e ) {
			return ResponseEntity.badRequest().body( new ResponseError( e.getMessage() ) );
		}
	}

	/**
	 * POST /set/max/efficiency
	 */
	@Timed
	@RequestMapping( value = "/set/max/efficiency", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity<?> maxEfficiencyOfDMUSet( @RequestBody SetDMUDTO dto ) throws URISyntaxException {
		log.debug( "REST request to set DMU maximization of efficiency" );

		try {

			CommonsValidation.REQUIRED_FIELD.validate( dto.getTargets(), "target DMUs" );
			CommonsValidation.REQUIRED_FIELD.validate( dto.getDmus(), "DMUs" );
			CommonsValidation.REQUIRED_FIELD.validate( dto.getKaoDistance(), "Distance between DEA and L2 efficiencies" );
			CommonsValidation.REQUIRED_FIELD.validate( dto.getCapital(), "capital" );
			CommonsValidation.REQUIRED_FIELD.validate( dto.getMode(), "mode to execute! (Altruistic or Selfish)" );

			return new ResponseEntity<>( capitalManagementService.maxEfficiencyOfDMUSet( dto.getDmus(), dto.getTargets(), dto.getCapital(), dto.getKaoDistance(), dto.getExecuteWithSuperEfficiency(), dto.getMode() ), HttpStatus.OK );
		} catch ( IOException e ) {
			return ResponseEntity.badRequest().body( new ResponseError( "It wasn possible to execute the maximization of efficiency of a set of dmus model!\nCause: " + e.getMessage() ) );
		} catch ( RuntimeException e ) {
			return ResponseEntity.badRequest().body( new ResponseError( e.getMessage() ) );
		}
	}

	/**
	 * POST /upload
	 */
	@Timed
	@ResponseStatus( HttpStatus.OK )
	@RequestMapping( value = "/upload", method = { RequestMethod.POST, RequestMethod.OPTIONS }, produces = MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity<?> deaCSV( @RequestParam( "file" ) MultipartFile file) throws URISyntaxException, IOException {
		log.debug( "REST request to upload csv file" );

		try {
			if ( file.isEmpty() )
				throw new RuntimeException( "The upload file is empty!" );

			Reader in = new InputStreamReader( file.getInputStream() );
			Iterable<CSVRecord> records = CSVFormat.EXCEL.parse( in );

			int dmuNameIndex = 0;
			List<String> inputNames = new ArrayList<>();
			List<String> outputNames = new ArrayList<>();

			boolean target;
			int line = 0, numberOfInputs = 0, numberOfOutputs = 0,
					expectedNumberOfColumns = 0;
			Double ks = 0d;

			Models model = null;
			UploadFileDTO result = new UploadFileDTO();

			List<DMU> dmus = new ArrayList<>();
			List<DMU> targets = new ArrayList<>();

			for ( CSVRecord record : records ) {
				if ( line == 0 ) {
					if ( record.size() != 3 )
						throw new RuntimeException( "The first line should have 3 columns, given, respectively, the code of the mode, the number of inputs and number of outputs." );

					String modelCode = record.get( 0 );
					if ( modelCode == null || modelCode.isEmpty() )
						throw new RuntimeException( "It's required to give the code of the model." );

					for ( Models m : Models.values() ) {
						if ( m.getCode().equals( modelCode ) ) {
							model = m;
							break;
						}
					}

					if ( model == null )
						throw new RuntimeException( "There's no model with the provided code." );

					String numberOfInputsString = record.get( 1 );
					if ( numberOfInputsString == null || numberOfInputsString.isEmpty() )
						throw new RuntimeException( "It's required to give the number of inputs" );

					try {
						numberOfInputs = Integer.valueOf( numberOfInputsString );
					} catch ( NumberFormatException e ) {
						throw new RuntimeException( "The given amount of inputs is not a valid integer" );
					}

					if ( numberOfInputs <= 0 )
						throw new RuntimeException( "The given amount of inputs must be greather than or equals to 1" );

					String numberOfOutputsString = record.get( 2 );
					if ( numberOfOutputsString == null || numberOfOutputsString.isEmpty() )
						throw new RuntimeException( "It's required to give the number of outputs" );

					try {
						numberOfOutputs = Integer.valueOf( numberOfOutputsString );
					} catch ( NumberFormatException e ) {
						throw new RuntimeException( "The given amount of outputs is not a valid integer" );
					}

					if ( numberOfOutputs <= 0 )
						throw new RuntimeException( "The given amount of outputs must be greather than or equals to 1" );

					expectedNumberOfColumns = 3 + ( 3 * numberOfInputs ) + ( 2 * numberOfOutputs );

					line++;
					continue;
				} else if ( line == 1 ) {

					model.collectData( result, record );

					line++;
					continue;
				} else if ( line == 2 ) {
					for ( int i = 0; i < numberOfInputs; i++ ) {
						String name = record.get( i + 1 );
						if ( name == null || name.isEmpty() )
							throw new RuntimeException( "It's required to give the name of all inputs" );

						inputNames.add( name );
					}

					for ( int i = 0; i < numberOfOutputs; i++ ) {
						String name = record.get( numberOfInputs + i + 1 );
						if ( name == null || name.isEmpty() )
							throw new RuntimeException( "It's required to give the name of all outputs" );

						outputNames.add( name );
					}

					line++;
					continue;
				}

				if ( record.size() != expectedNumberOfColumns ) {
					throw new RuntimeException( "The line '" + line + "' do not have the expected number of columns!(Expected = '" + expectedNumberOfColumns + "', Actual = '" + record.size() + "')" );
				}

				DMU dmu = new DMU();
				dmu.setName( record.get( dmuNameIndex ) );
				dmu.setInputs( new ArrayList<>( numberOfInputs ) );
				dmu.setOutputs( new ArrayList<>( numberOfOutputs ) );

				String targetString = record.get( 1 );
				if ( targetString == null || targetString.isEmpty() )
					throw new RuntimeException( "It's required inform whether the dmu is target or not" );

				target = Boolean.valueOf( targetString );

				String ksString = record.get( 2 );
				if ( ksString != null && !ksString.isEmpty() ) {
					try {
						ks = Double.valueOf( ksString );
					} catch ( NumberFormatException e ) {
						throw new RuntimeException( String.format( "The given coefficient of size update '%s' for the DMU '%s' at line '%d' is not a valid double number.", record.get( 2 ), dmu.getName(), line + 1 ) );
					}
				}

				dmu.setCoefficientSize( ks );

				for ( int i = 0; i < numberOfInputs; i++ ) {
					Input input = new Input();

					input.setName( inputNames.get( i ) );

					String value = record.get( ( i * 3 ) + 3 );
					if ( value == null || value.isEmpty() )
						throw new RuntimeException( "It's required inform the value of all inputs" );

					try {
						input.setValue( Double.valueOf( value ) );
					} catch ( NumberFormatException e ) {
						throw new RuntimeException( "The value '" + value + "' of the input '" + inputNames.get( i ) + "' is not a valid float number!" );
					}

					String costString = record.get( ( i * 3 ) + 4 );
					if ( costString == null || costString.isEmpty() )
						throw new RuntimeException( "It's required to give the cost of the input '" + input.getName() + "'" );

					try {
						input.setCost( Double.valueOf( costString ) );
					} catch ( NumberFormatException e ) {
						throw new RuntimeException( "The value '" + costString + "' of the input '" + inputNames.get( i ) + "' is not a valid float number!" );
					}

					String boundString = record.get( ( i * 3 ) + 5 );
					if ( boundString != null && !boundString.isEmpty() ) {
						try {
							input.setBound( Double.valueOf( boundString ) );
						} catch ( NumberFormatException e ) {
							throw new RuntimeException( "The value '" + boundString + "' of the input '" + inputNames.get( i ) + "' is not a valid float number!" );
						}
					}

					dmu.getInputs().add( input );
				}

				for ( int i = 0; i < numberOfOutputs; i++ ) {
					int index = 3 + ( numberOfInputs * 3 ) + ( i * 2 );

					Output output = new Output();
					output.setName( outputNames.get( i ) );

					String value = record.get( index );
					if ( value == null || value.isEmpty() )
						throw new RuntimeException( "It's required inform the value of all outputs" );

					try {
						output.setValue( Double.valueOf( value ) );
					} catch ( NumberFormatException e ) {
						throw new RuntimeException( "The value '" + record.get( index ) + "' of the output '" + outputNames.get( i ) + "' is not a valid float number!" );
					}

					String boundString = record.get( index + 1 );
					if ( boundString != null && !boundString.isEmpty() ) {
						try {
							output.setBound( Double.valueOf( boundString ) );
						} catch ( NumberFormatException e ) {
							throw new RuntimeException( "The bound '" + boundString + "' of the output '" + outputNames.get( i ) + "' is not a valid float number!" );
						}
					}

					dmu.getOutputs().add( output );
				}

				dmus.add( dmu );
				if ( target )
					targets.add( dmu );

				line++;
			}

			in.close();

			if ( dmus == null || dmus.size() < 2 ) {
				throw new RuntimeException( "It's necessary give at least 2 DMUs for DEA calculation" );
			}

			System.out.println( "******** DEBUG ********" );
			System.out.println( Arrays.deepToString( dmus.toArray() ) );
			System.out.println( "******** DEBUG ********" );

			List<DMU> dea = capitalManagementService.dea( dmus );
			dea = capitalManagementService.superEficiencyDEA( dmus );
			KAOSolution kao = (KAOSolution) capitalManagementService.kao( dea );

			result.setSolution( kao );
			result.setTargets( targets );
			result.setModelCode( model.getCode() );

			return new ResponseEntity<>( result, HttpStatus.OK );

		} catch ( RuntimeException e ) {
			return ResponseEntity.badRequest().body( new ResponseError( e.getMessage() ) );
		}
	}
}