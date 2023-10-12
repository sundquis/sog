/**
 * Copyright (C) 2021, 2023
 * *** *** *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 * *** *** * 
 * Sundquist
 */
package sog.util;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import sog.core.AppRuntime;
import sog.core.Assert;
import sog.core.Parser;
import sog.core.Property;
import sog.core.Test;

/**
 * @author sundquis
 *
 */
@Test.Subject( "test." )
public class Macro implements Function<String, Stream<String>> {
	
	/* Maximum recursive depth allowed */
	private static final int MAX_ITERATIONS = Property.get( "maxIterations",  100,  Parser.INTEGER );

	/*
	 * The pattern for matching key occurrences of the form:
	 * 		$ { key }
	 * 
	 * Elements:
	 * 		\\$				Dollar signals start
	 * 		\\s*			Optional white space
	 * 		\\{				Left brace
	 * 		\\s*			Optional white space
	 * 		(				Start of a group to capture the key name
	 * 			[^			Negated character class, not the following:
	 * 				\\$		Dollar
	 * 				\\{		Left brace
	 * 				\\}		Right brace
	 * 			]*			Zero or more. Won't start with whitespace because preceded by greedy \\s*
	 * 			[^			Negated character class, not the following:
	 * 				\\$		Dollar
	 * 				\\{		Left brace
	 * 				\\}		Right brace\
	 * 				\\s		White space
	 * 			]			One instance required. Key name can't end with whitespace
	 * 		)				End capture group. Matches the key name
	 * 		\\s*			Optional white space
	 * 		\\}				Right brace
	 */
	private static final String REGEX = "\\$\\s*\\{\\s*([^\\$\\{\\}]*[^\\$\\{\\}\\s])\\s*\\}";

	/* All instances share a compiled pattern */
	private static final Pattern PATTERN = Pattern.compile( REGEX );
	
	/* Predicate defining a legal String for keys */
	private static boolean isLegal( String key ) {
		return ! (
			key == null || key.isEmpty() || key.matches( "^\\s.*" ) || key.matches( ".*\\s$" )
				|| key.contains( "$" ) || key.contains( "{" ) || key.contains( "}" )
		);
	}


	
	

	/* 
	 * A map representing the expansion rules. The <key, value> pairs correspond to:
	 * 		String  		the key name to match
	 * 		List<Object> 	the list of replacements, via Object.toString()
	 * The input line is copied, once for each replacement.
	 */
	private final Map<String, List<String>> expansions = new TreeMap<>();
	
	/* Reusable matcher */
	private final Matcher matcher = Macro.PATTERN.matcher( "" );

	
	/**
	 * Constructs a stateless instance. Expansion rules are added via chained methods.
	 */
	@Test.Decl( "Stateless instance acts as identity function" )
	public Macro() { }
	
	
	/**
	 * Register an expansion rule. Empty replacement sequence are allowed; this has
	 * the effect of removing the current line from the output stream.
	 * 
	 * @param key The nonempty key name to match.
	 * @param values The sequence of replacement values to use.
	 * @return This {@code Macro} instance for chaining.
	 */
	@Test.Decl( "Throws AssertionError for empty key" )
	@Test.Decl( "Dollar wihtin key not allowed" )
	@Test.Decl( "Left brace in key not allowed" )
	@Test.Decl( "Right brace in key not allowed" )
	@Test.Decl( "Key does not start with white space" )
	@Test.Decl( "Key does not end with white space" )
	@Test.Decl( "Whitespace in key is allowed" )
	@Test.Decl( "Special characters in key allowed" )
	@Test.Decl( "Empty value sequence allowed" )
	@Test.Decl( "Throws assertion error for a null value" )
	@Test.Decl( "Any value may be empty" )
	@Test.Decl( "Replacement rules can be over-written" )
	@Test.Decl( "Returns this instance" )
	public Macro expand( String key, String... values ) {
		Assert.isTrue( Macro.isLegal( key ) );
		Assert.nonNull( values );
		Assert.isTrue( Stream.of( values ).allMatch( s -> s != null ) );

		this.expansions.put( key, Arrays.asList( values ) );
		return this;
	}

	
	/**
	 * Register an expansion rule. Empty replacement collections are allowed; this has
	 * the effect of removing the current line from the output stream.
	 * 
	 * For restrictions on the key 
	 * @see Macro#expand(String, String...)
	 * 
	 * @param key 		The nonempty key name to match.
	 * @param values 	The collection of replacement values to use.
	 * @return 			This {@code Macro} instance for chaining.
	 */
	@Test.Decl( "Throws assertion error for null collection of values" )
	@Test.Decl( "Empty value collection allowed" )
	@Test.Decl( "Any value may be empty" )
	@Test.Decl( "Throws assertion error for a null value" )
	@Test.Decl( "Returns this instance" )
	public Macro expand( String key, List<String> values ) {
		Assert.isTrue( Macro.isLegal( key ) );
		Assert.nonNull( values );
		Assert.isTrue( values.stream().allMatch( s -> s != null ) );

		this.expansions.put( key, values );
		return this;
	}

	/**
	 * Register an expansion rule where the given key will be sequentially replaced by
	 * the values in a given list.
	 * 
	 * For restrictions on the key @see Macro#expand(String, String...)
	 * 
	 * @param <T>		Type of elements in the list of replacements.
	 * @param key		The non-empty key to match.
	 * @param values	The list of replacements.
	 * @param mapper	Mapper to convert objects to string representations.
	 * @return			This {@code Macro} instance to allow chaining.
	 */
	@Test.Decl( "Throws assertion error for null collection of values" )
	@Test.Decl( "Empty value collection allowed" )
	@Test.Decl( "Throws AssertionError for null mapper" )
	@Test.Decl( "Throws assertion error for a null mapper value" )
	@Test.Decl( "Returns this Macro instance to allow chaining" )
	public <T> Macro expand( String key, List<T> values, Function<? super T, String> mapper ) {
		Assert.isTrue( Macro.isLegal( key ) );
		Assert.nonNull( values );
		Assert.isTrue( values.stream().allMatch( s -> s != null ) );
		
		this.expansions.put( key, values.stream().map( mapper ).collect( Collectors.toList() ) );
		return this;
	}
	
	
	/**
	 * Apply expansion rules to the given line to produce a stream of expanded lines. 
	 * Each occurrence of a key pattern of the form
	 * 		${key}
	 * is replaced using the registered expansion rule. An exception is thrown if no rule is 
	 * registered for a matched key unless diagnostic output has been enabled, in which case
	 * an error message is printed. If the expansion rule specifies an empty set of replacements
	 * then an empty stream is returned. If an expansion rule specifies multiple replacements
	 * then the line is replicated, once for each replacement. Rules are applied iteratively until
	 * all macro patterns have been expanded, or the maximum number of iterations is reached.
	 * {@code Macro} can be used as a "mapper" in {@link java.util.stream.Stream#flatMap} to
	 * expand all lines in a stream of strings:
	 * 		someStringStream.flatMap( this )...
	 * 
	 * @see java.util.function.Function#apply(java.lang.Object)
	 * 
	 * @param line The input line to be expanded.
	 * @return A {@code Stream} containing the results of applying expansion rules for each macro key.
	 */
	@Override 
	@Test.Decl( "Throws AssertionError for null line" )
	@Test.Decl( "Input line without keys is unchanged" )
	@Test.Decl( "Throws AppRuntime for excessive recursion" )
	@Test.Decl( "An empty expansion rule produces an empty stream" )
	@Test.Decl( "Throws AppExcpetion for missing key" )
	@Test.Decl( "Simple replacement works" )
	@Test.Decl( "Multiple keys replaced" )
	@Test.Decl( "All keys replaced in output stream" )
	@Test.Decl( "Works with flatMap to expand a stream" )
	@Test.Decl( "Replacement within replacement allowed" )
	@Test.Decl( "Mal-formed patterns are ignored" )
	@Test.Decl( "One letter keys work" )
	@Test.Decl( "Multiple lines in output for multiple replacements" )
	@Test.Decl( "Can trigger recursion exception through excessive replacements" )
	public Stream<String> apply( String line ) {
		Assert.nonNull( line );
		List<String> results = new LinkedList<>();
		
		try ( Queue<String> pending = new FifoQueue<>() ) {
			pending.put( line );
			int length = 1;  // Guard against infinite self-reference
			String current;
			while ( (current = pending.get()) != null ) {
				length--;	// FIXME: Could add size() to Queue
				if ( this.matcher.reset( current ).find() ) {
					String head = current.substring( 0,  this.matcher.start() );
					String key = this.matcher.group( 1 );
					String tail = current.substring( this.matcher.end() );
					List<String> expansions = this.getExpansions( key );
					length += expansions.size();
					expansions.forEach( s -> pending.put( head + s + tail ) );
				} else {
					results.add( current );
				}
				if ( length > Macro.MAX_ITERATIONS ) {
					throw new AppRuntime( "Infinite recurrsion detected: " + line );
				}
			}			
		}

		return results.stream();
	}

	/* Helper to look up expansion and handle missing mapping */
	private List<String> getExpansions( String key ) {
		List<String> result = this.expansions.get( key );
		if ( result == null ) {
			throw new AppRuntime( "Missing key: " + key );
		}
		return result;
	}


	/**
	 * Construct a stream consisting of the expansion of each of the given lines of input.
	 * This is equivalent to
	 * 		{@code Stream.of( lines ).flatMap( this )}
	 * 
	 * For restrictions
	 * @see Macro#expand(String, String...)
	 * 
	 * @param lines A sequence of lines to expand.
	 * @return A {@code Stream} containing the results of applying expansion rules for each macro key.
	 */
	@Test.Decl( "An empty sequence of lines produces an empty stream" )
	@Test.Decl( "Throws AppRuntime for excessive recursion" )
	@Test.Decl( "An empty expansion rule removes line from output stream" )
	@Test.Decl( "Multiple lines in output for multiple input lines" )
	public Stream<String> apply( String ... lines ) {
		return Stream.of( lines ).flatMap( this );
	}

	
	/**
	 * Construct a stream consisting of the expansion of each of the given lines of input.
	 * This is equivalent to
	 * 		{@code lines.stream().flatMap( this )}
	 * 
	 * For restrictions
	 * @see Macro#expand(String, String...)
	 * 
	 * @param lines A collection of lines to expand.
	 * @return A {@code Stream} containing the results of applying expansion rules for each macro key.
	 */
	@Test.Decl( "An empty collection produces an empty stream" )
	@Test.Decl( "Throws Assertion Error for null lines" )
	@Test.Decl( "Throws AppRuntime for excessive recursion" )
	@Test.Decl( "An empty expansion rule removes line from output stream" )
	@Test.Decl( "Multiple lines in output for multiple input lines" )
	public Stream<String> apply( List<String> lines ) {
		Assert.nonNull( lines );
		return lines.stream().flatMap( this );
	}
	

}
