#!/bin/bash
#
# customize.sh - Portlet customization script
#
# Author: Riccardo Bruno <riccardo.bruno@ct.infn.it>
#

# Customize below settings
PROJECT_NAME="TemplateSpecialJobPortlet"
PORTLET_CATEGORY_NAME="Sci-GaIA"
PORTLET_DISPLAY_NAME="Sci-GaIA Template Special Job Portlet"
PORTLET_NAME="TemplateSpecialJobPortlet"
PORTLET_CLASSWRAPPER="template-special-job-portlet"
MYPORTLET_CLASS="TemplateSpecialJobPortlet"
PORTLET_TITLE="Sci-GaIA Template Special Job Portlet"
PORTLET_SHTITLE="Sci-GaIA Template SpecialJob Portlet"
PORTLET_KEYWORKDS="Template Special Job Portlet"

# Files to customize
PACKAGE_PATH=./docroot/WEB-INF/src/it/dfa/unict
LIFERAY_DISPLAY=./docroot/WEB-INF/liferay-display.xml
LIFERAY_PORTLET=./docroot/WEB-INF/liferay-portlet.xml
PORTLET=./docroot/WEB-INF/portlet.xml
PORTLET_CLASS=./docroot/WEB-INF/src/it/dfa/unict/TemplateSpecialJobPortlet.java
CONFIG_CLASS=./docroot/WEB-INF/src/it/dfa/unict/ConfigurationActionImpl.java
VIEW_PILOT_JSP=./docroot/jsps/portlet-config/view-pilot.jsp
BUILD_FILE=build.xml
#
# Function that replace the 1st  matching occurrence of
# a pattern with a given line into the specified filename
#
replace_line() {
  file_name=$1   # File to change
  pattern=$2     # Matching pattern that identifies the line
  new_line=$3    # New line content
  keep_suffix=$4 # Optionally specify a suffix to keep a safe copy

  if [ "$file_name" != "" -a -f $file_name -a "$pattern" != "" ]; then
	  TMP=$(mktemp)
	  cp $file_name $TMP
	  if [ "$keep_suffix" != "" ]; then # keep a copy of replaced file
		  cp $file_name $file_name"_"$keep_suffix
	  fi
	  MATCHING_LINE=$(cat $TMP | grep -n "$pattern" | head -n 1 | awk -F':' '{ print $1 }' | xargs echo)
	  if [ "$MATCHING_LINE" != "" ]; then
		  cat $TMP | head -n $((MATCHING_LINE-1)) > $file_name
		  echo -e "$new_line\n" >> $file_name
		  cat $TMP | tail -n +$((MATCHING_LINE+1)) >> $file_name
	  else
		  echo "WARNING: Did not find '"$pattern"' in file: '"$file_name"'"
	  fi
	  rm -f $TMP
  else
	  echo "You must provide an existing filename and a valid pattern"
	  return 1
  fi
}


# liferay-display.xml
REPL_LINE="<category name=\"Sci-GaIA\">"
NEW_LINE="\t<category name=\"$PORTLET_CATEGORY_NAME\">"
replace_line $LIFERAY_DISPLAY "$REPL_LINE" "$NEW_LINE" "orig"
REPL_LINE="<portlet id=\"TemplateSpecialJobPortlet\" />"
NEW_LINE="\t\t<portlet id=\"$PORTLET_NAME\" />"
replace_line $LIFERAY_DISPLAY "$REPL_LINE" "$NEW_LINE"

# liferay-portlet.xml
REPL_LINE="<portlet-name>TemplateSpecialJobPortlet</portlet-name>"
NEW_LINE="\t\t<portlet-name>$PORTLET_NAME</portlet-name>"
replace_line $LIFERAY_PORTLET "$REPL_LINE" "$NEW_LINE" "orig"
REPL_LINE="<css-class-wrapper>template-special-job-portlet</css-class-wrapper>"
NEW_LINE="\t\t<css-class-wrapper>$PORTLET_CLASSWRAPPER</css-class-wrapper>"
replace_line $LIFERAY_PORTLET "$REPL_LINE" "$NEW_LINE"

# portlet.xml
REPL_LINE="<portlet-name>TemplateSpecialJobPortlet</portlet-name>"
NEW_LINE="\t\t<portlet-name>$PORTLET_NAME</portlet-name>"
replace_line $PORTLET "$REPL_LINE" "$NEW_LINE" "orig"
REPL_LINE="<display-name>Sci-GaIA Template Special Job Portlet</display-name>"
NEW_LINE="\t\t<display-name>$PORTLET_DISPLAY_NAME</display-name>"
replace_line $PORTLET "$REPL_LINE" "$NEW_LINE"
REPL_LINE="<portlet-class>it.dfa.unict.TemplateSpecialJobPortlet</portlet-class>"
NEW_LINE="\t\t<portlet-class>it.dfa.unict.$MYPORTLET_CLASS</portlet-class>"
replace_line $PORTLET "$REPL_LINE" "$NEW_LINE"
REPL_LINE="<title>Sci-GaIA Template Special Job Portlet</title>"
NEW_LINE="\t\t<title>$PORTLET_TITLE</title>"
replace_line $PORTLET "$REPL_LINE" "$NEW_LINE"
REPL_LINE="<short-title>Sci-GaIA Template SpecialJob Portlet</short-title>"
NEW_LINE="\t\t<short-title>$PORTLET_SHTITLE</short-title>"
replace_line $PORTLET "$REPL_LINE" "$NEW_LINE"
REPL_LINE="<keywords>Template Special Job Portlet</keywords>"
NEW_LINE="\t\t<keywords>$PORTLET_KEYWORKDS</keywords>"
replace_line $PORTLET "$REPL_LINE" "$NEW_LINE"

# HostnamePortlet.java
REPL_LINE="public class TemplateSpecialJobPortlet extends MVCPortlet {"
NEW_LINE="public class $MYPORTLET_CLASS extends MVCPortlet {"
replace_line $PORTLET_CLASS "$REPL_LINE" "$NEW_LINE"
REPL_LINE="private final Log _log = LogFactoryUtil.getLog(TemplateSpecialJobPortlet.class);"
NEW_LINE="private final Log _log = LogFactoryUtil.getLog($MYPORTLET_CLASS.class);"
replace_line $PORTLET_CLASS "$REPL_LINE" "$NEW_LINE" "orig"
mv "$PORTLET_CLASS" "$PACKAGE_PATH/$PORTLET_NAME.java"

# ConfigurationActionImpl.java
REPL_LINE="Utils.string2File(TemplateSpecialJobPortlet.pilotScript, pilotScript);"
NEW_LINE="Utils.string2File($MYPORTLET_CLASS.pilotScript, pilotScript);"
replace_line $CONFIG_CLASS "$REPL_LINE" "$NEW_LINE"

# view-pilot.jsp
REPL_LINE="<%@page import=\"it.dfa.unict.TemplateSpecialJobPortlet\"%>"
NEW_LINE="<%@page import=\"it.dfa.unict.$MYPORTLET_CLASS\"%>"
replace_line $VIEW_PILOT_JSP "$REPL_LINE" "$NEW_LINE" "orig"
REPL_LINE="String pilotFilePath = TemplateSpecialJobPortlet.pilotScript;"
NEW_LINE="String pilotFilePath = $MYPORTLET_CLASS.pilotScript;"
replace_line $VIEW_PILOT_JSP "$REPL_LINE" "$NEW_LINE" "orig"

# build.xml
REPL_LINE="<project name=\"template-special-job-portlet\" basedir=\".\" default=\"deploy\">"
NEW_LINE="<project name=\"$PROJECT_NAME\" basedir=\".\" default=\"deploy\">"
replace_line $BUILD_FILE "$REPL_LINE" "$NEW_LINE" "orig"
