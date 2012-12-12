/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache license, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the license for the specific language governing permissions and
 * limitations under the license.
 */
package org.apache.logging.log4j.core.pattern;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.layout.PatternLayout;

/**
 * Style pattern converter. Adds ANSI color styling to the result of the enclosed pattern.
 */
public abstract class AbstractStyleNameConverter extends LogEventPatternConverter {

    /**
     * Black style pattern converter. Adds ANSI color styling to the result of the enclosed pattern.
     */
    @Plugin(name = Black.NAME, type = "Converter")
    @ConverterKeys(Black.NAME)
    public static final class Black extends AbstractStyleNameConverter {

        protected static final String NAME = "black";

        /**
         * Gets an instance of the class (called via reflection).
         *
         * @param config
         *            The current Configuration.
         * @param options
         *            The pattern options, may be null. If the first element is "short", only the first line of the throwable will be
         *            formatted.
         * @return new instance of class or null
         */
        public static Black newInstance(final Configuration config, final String[] options) {
            return newInstance(Black.class, NAME, config, options);
        }

        /**
         * Constructs the converter. This constructor must be public.
         *
         * @param formatters
         *            The PatternFormatters to generate the text to manipulate.
         * @param styling
         *            The styling that should encapsulate the pattern.
         */
        public Black(final List<PatternFormatter> formatters, final String styling) {
            super(NAME, formatters, styling);
        }
    }

    /**
     * Blue style pattern converter. Adds ANSI color styling to the result of the enclosed pattern.
     */
    @Plugin(name = Blue.NAME, type = "Converter")
    @ConverterKeys(Blue.NAME)
    public static final class Blue extends AbstractStyleNameConverter {

        protected static final String NAME = "blue";

        /**
         * Gets an instance of the class (called via reflection).
         *
         * @param config
         *            The current Configuration.
         * @param options
         *            The pattern options, may be null. If the first element is "short", only the first line of the throwable will be
         *            formatted.
         * @return new instance of class or null
         */
        public static Blue newInstance(final Configuration config, final String[] options) {
            return newInstance(Blue.class, NAME, config, options);
        }

        /**
         * Constructs the converter. This constructor must be public.
         *
         * @param formatters
         *            The PatternFormatters to generate the text to manipulate.
         * @param styling
         *            The styling that should encapsulate the pattern.
         */
        public Blue(final List<PatternFormatter> formatters, final String styling) {
            super(NAME, formatters, styling);
        }
    }

    /**
     * Cyan style pattern converter. Adds ANSI color styling to the result of the enclosed pattern.
     */
    @Plugin(name = Cyan.NAME, type = "Converter")
    @ConverterKeys(Cyan.NAME)
    public static final class Cyan extends AbstractStyleNameConverter {

        protected static final String NAME = "cyan";

        /**
         * Gets an instance of the class (called via reflection).
         *
         * @param config
         *            The current Configuration.
         * @param options
         *            The pattern options, may be null. If the first element is "short", only the first line of the throwable will be
         *            formatted.
         * @return new instance of class or null
         */
        public static Cyan newInstance(final Configuration config, final String[] options) {
            return newInstance(Cyan.class, NAME, config, options);
        }

        /**
         * Constructs the converter. This constructor must be public.
         *
         * @param formatters
         *            The PatternFormatters to generate the text to manipulate.
         * @param styling
         *            The styling that should encapsulate the pattern.
         */
        public Cyan(final List<PatternFormatter> formatters, final String styling) {
            super(NAME, formatters, styling);
        }
    }

    /**
     * Green style pattern converter. Adds ANSI color styling to the result of the enclosed pattern.
     */
    @Plugin(name = Green.NAME, type = "Converter")
    @ConverterKeys(Green.NAME)
    public static final class Green extends AbstractStyleNameConverter {

        protected static final String NAME = "green";

        /**
         * Gets an instance of the class (called via reflection).
         *
         * @param config
         *            The current Configuration.
         * @param options
         *            The pattern options, may be null. If the first element is "short", only the first line of the throwable will be
         *            formatted.
         * @return new instance of class or null
         */
        public static Green newInstance(final Configuration config, final String[] options) {
            return newInstance(Green.class, NAME, config, options);
        }

        /**
         * Constructs the converter. This constructor must be public.
         *
         * @param formatters
         *            The PatternFormatters to generate the text to manipulate.
         * @param styling
         *            The styling that should encapsulate the pattern.
         */
        public Green(final List<PatternFormatter> formatters, final String styling) {
            super(NAME, formatters, styling);
        }
    }

    /**
     * Magenta style pattern converter. Adds ANSI color styling to the result of the enclosed pattern.
     */
    @Plugin(name = Magenta.NAME, type = "Converter")
    @ConverterKeys(Magenta.NAME)
    public static final class Magenta extends AbstractStyleNameConverter {

        protected static final String NAME = "magenta";

        /**
         * Gets an instance of the class (called via reflection).
         *
         * @param config
         *            The current Configuration.
         * @param options
         *            The pattern options, may be null. If the first element is "short", only the first line of the throwable will be
         *            formatted.
         * @return new instance of class or null
         */
        public static Magenta newInstance(final Configuration config, final String[] options) {
            return newInstance(Magenta.class, NAME, config, options);
        }

        /**
         * Constructs the converter. This constructor must be public.
         *
         * @param formatters
         *            The PatternFormatters to generate the text to manipulate.
         * @param styling
         *            The styling that should encapsulate the pattern.
         */
        public Magenta(final List<PatternFormatter> formatters, final String styling) {
            super(NAME, formatters, styling);
        }
    }

    /**
     * Red style pattern converter. Adds ANSI color styling to the result of the enclosed pattern.
     */
    @Plugin(name = Red.NAME, type = "Converter")
    @ConverterKeys(Red.NAME)
    public static final class Red extends AbstractStyleNameConverter {

        protected static final String NAME = "red";

        /**
         * Gets an instance of the class (called via reflection).
         *
         * @param config
         *            The current Configuration.
         * @param options
         *            The pattern options, may be null. If the first element is "short", only the first line of the throwable will be
         *            formatted.
         * @return new instance of class or null
         */
        public static Red newInstance(final Configuration config, final String[] options) {
            return newInstance(Red.class, NAME, config, options);
        }

        /**
         * Constructs the converter. This constructor must be public.
         *
         * @param formatters
         *            The PatternFormatters to generate the text to manipulate.
         * @param styling
         *            The styling that should encapsulate the pattern.
         */
        public Red(final List<PatternFormatter> formatters, final String styling) {
            super(NAME, formatters, styling);
        }
    }

    /**
     * White style pattern converter. Adds ANSI color styling to the result of the enclosed pattern.
     */
    @Plugin(name = White.NAME, type = "Converter")
    @ConverterKeys(White.NAME)
    public static final class White extends AbstractStyleNameConverter {

        protected static final String NAME = "white";

        /**
         * Gets an instance of the class (called via reflection).
         *
         * @param config
         *            The current Configuration.
         * @param options
         *            The pattern options, may be null. If the first element is "short", only the first line of the throwable will be
         *            formatted.
         * @return new instance of class or null
         */
        public static White newInstance(final Configuration config, final String[] options) {
            return newInstance(White.class, NAME, config, options);
        }

        /**
         * Constructs the converter. This constructor must be public.
         *
         * @param formatters
         *            The PatternFormatters to generate the text to manipulate.
         * @param styling
         *            The styling that should encapsulate the pattern.
         */
        public White(final List<PatternFormatter> formatters, final String styling) {
            super(NAME, formatters, styling);
        }
    }

    /**
     * Yellow style pattern converter. Adds ANSI color styling to the result of the enclosed pattern.
     */
    @Plugin(name = Yellow.NAME, type = "Converter")
    @ConverterKeys(Yellow.NAME)
    public static final class Yellow extends AbstractStyleNameConverter {

        protected static final String NAME = "yellow";

        /**
         * Gets an instance of the class (called via reflection).
         *
         * @param config
         *            The current Configuration.
         * @param options
         *            The pattern options, may be null. If the first element is "short", only the first line of the throwable will be
         *            formatted.
         * @return new instance of class or null
         */
        public static Yellow newInstance(final Configuration config, final String[] options) {
            return newInstance(Yellow.class, NAME, config, options);
        }

        /**
         * Constructs the converter.
         *
         * @param formatters
         *            The PatternFormatters to generate the text to manipulate.
         * @param styling
         *            The styling that should encapsulate the pattern.
         */
        public Yellow(final List<PatternFormatter> formatters, final String styling) {
            super(NAME, formatters, styling);
        }
    }

    /**
     * Gets an instance of the class (called via reflection).
     *
     * @param config
     *            The current Configuration.
     * @param options
     *            The pattern options, may be null. If the first element is "short", only the first line of the throwable will be formatted.
     * @return new instance of class or null
     */
    protected static <T extends AbstractStyleNameConverter> T newInstance(final Class<T> asnConverterClass, final String name, final Configuration config,
            final String[] options) {
        final List<PatternFormatter> formatters = toPatternFormatterList(config, options);
        if (formatters == null) {
            return null;
        }
        try {
            final Constructor<T> constructor = asnConverterClass.getConstructor(List.class, String.class);
            return constructor.newInstance(formatters, AnsiEscape.createSequence(name));
        } catch (final SecurityException e) {
            LOGGER.error(e.toString(), e);
        } catch (final NoSuchMethodException e) {
            LOGGER.error(e.toString(), e);
        } catch (final IllegalArgumentException e) {
            LOGGER.error(e.toString(), e);
        } catch (final InstantiationException e) {
            LOGGER.error(e.toString(), e);
        } catch (final IllegalAccessException e) {
            LOGGER.error(e.toString(), e);
        } catch (final InvocationTargetException e) {
            LOGGER.error(e.toString(), e);
        }
        return null;
    }

    /**
     * Creates a list of PatternFormatter from the given configuration and options or null if no pattern is supplied.
     *
     * @param config
     *            A configuration
     * @param options
     *            pattern options
     * @return a list of PatternFormatter from the given configuration and options or null if no pattern is supplied.
     */
    private static List<PatternFormatter> toPatternFormatterList(final Configuration config, final String[] options) {
        if (options.length == 0 || options[0] == null) {
            LOGGER.error("No pattern supplied on style for config=" + config);
            return null;
        }
        final PatternParser parser = PatternLayout.createPatternParser(config);
        if (parser == null) {
            LOGGER.error("No PatternParser created for config=" + config + ", options=" + options);
            return null;
        }
        return parser.parse(options[0]);
    }

    private final List<PatternFormatter> formatters;

    private final String style;

    /**
     * Constructs the converter.
     *
     * @param formatters
     *            The PatternFormatters to generate the text to manipulate.
     * @param styling
     *            The styling that should encapsulate the pattern.
     */
    protected AbstractStyleNameConverter(final String name, final List<PatternFormatter> formatters, final String styling) {
        super(name, "style");
        this.formatters = formatters;
        this.style = styling;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        final StringBuilder buf = new StringBuilder();
        for (final PatternFormatter formatter : formatters) {
            formatter.format(event, buf);
        }
        if (buf.length() > 0) {
            toAppendTo.append(style).append(buf.toString()).append(AnsiEscape.getDefaultStyle());
        }
    }
}