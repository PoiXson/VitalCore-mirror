#!/usr/bin/bash
## ================================================================================
##  xBuild
## Copyright (c) 2019-2025 Mattsoft/PoiXson
## <https://mattsoft.net> <https://poixson.com>
## Released under the AGPL 3.0 + ADD-PXN-V1
##
## Description: Auto compile a project and build rpms
##
## ================================================================================
##
## This program is free software: you can redistribute it and/or modify it under
## the terms of the GNU Affero General Public License + ADD-PXN-V1 as published by
## the Free Software Foundation, either version 3 of the License, or (at your
## option) any later version, with the addition of ADD-PXN-V1.
##
## This program is distributed in the hope that it will be useful, but WITHOUT ANY
## WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
## PARTICULAR PURPOSE.
##
## See the GNU Affero General Public License for more details
## <http://www.gnu.org/licenses/agpl-3.0.en.html> and Addendum ADD-PXN-V1
## <https://dl.poixson.com/ADD-PXN-V1.txt>
##
## **ADD-PXN-V1 - Addendum to the GNU Affero General Public License (AGPL)**
## This Addendum is an integral part of the GNU Affero General Public License
## (AGPL) under which this software is licensed. By using, modifying, or
## distributing this software, you agree to the following additional terms:
##
## 1. **Source Code Availability:** Any distribution of the software, including
##    modified versions, must include the complete corresponding source code. This
##    includes all modifications made to the original source code.
## 2. **Free of Charge and Accessible:** The source code and any modifications to
##    the source code must be made available to all with reasonable access to the
##    internet, free of charge. No fees may be charged for access to the source
##    code or for the distribution of the software, whether in its original or
##    modified form. The source code must be accessible in a manner that allows
##    users to easily obtain, view, and modify it. This can be achieved by
##    providing a link to a publicly accessible repository (e.g., GitHub, GitLab)
##    or by including the source code directly with the distributed software.
## 3. **Documentation of Changes:** When distributing modified versions of the
##    software, you must provide clear documentation of the changes made to the
##    original source code. This documentation should be included with the source
##    code, and should be easily accessible to users.
## 4. **No Additional Restrictions:** You may not impose any additional
##    restrictions on the rights granted by the AGPL or this Addendum. All
##    recipients of the software must have the same rights to use, modify, and
##    distribute the software as granted under the AGPL and this Addendum.
## 5. **Acceptance of Terms:** By using, modifying, or distributing this software,
##    you acknowledge that you have read, understood, and agree to comply with the
##    terms of the AGPL and this Addendum.
## ================================================================================
# copy-base.sh



source /usr/bin/pxn/scripts/common.sh  || exit 1
echo



if [[ -z $WDIR ]]; then
	echo
	failure "Failed to find current working directory"
	failure ; exit 1
fi



PATH_DEST=$( \realpath "$WDIR/.." )
if [[ -z $PATH_DEST ]]; then
	failure "Failed to find project root"
	failure ; exit 1
fi
let COUNT_TOTAL=0
let COUNT_COPY=0



function DoCopyServerFiles() {
	local TITLE="$1"
	local NAME="$2"
	local PATH_DEST_NAME="$PATH_DEST/$NAME"
	title C "$TITLE"
	# resources/
	\pushd  "$WDIR/resources/"  >/dev/null  || exit 1
		DoCopyIfDifferent  "logo.svg"  "$PATH_DEST_NAME/resources/logo.svg"  || exit 1
		DoCopyIfDifferent  "logo.png"  "$PATH_DEST_NAME/resources/logo.png"  || exit 1
	\popd >/dev/null
	# resources/languages/
	\pushd  "$WDIR/resources/languages/"  >/dev/null  || exit 1
		DoCopyIfDifferent  "en.json"  "$PATH_DEST_NAME/resources/languages/en.json"  || exit 1
	\popd >/dev/null
	# src/
	\pushd  "$WDIR/src/"  >/dev/null  || exit 1
		local TEMP_FILE=$( \mktemp )
		if [[ -z $TEMP_FILE   ]] \
		|| [[ ! -f $TEMP_FILE ]]; then
			failure "Failed to create a temp file for: $NAME"
			failure ; exit 1
		fi
		echo    "// Generated for: $TITLE"           >"$TEMP_FILE"
		echo -n "// "                               >>"$TEMP_FILE"
		\date                                       >>"$TEMP_FILE"
		\cat  "$WDIR/src/PoiXsonPluginLoader.java"  >>"$TEMP_FILE"
		DoCopyIfDifferent  "$TEMP_FILE"  "$PATH_DEST_NAME/src/com/poixson/PoiXsonPluginLoader.java"  || exit 1
		\rm -f "$TEMP_FILE"
	\popd >/dev/null
}

function DoCopyFile() {
	local FILE_A="$1" ; [[ -z $FILE_A ]] && exit 1
	local FILE_B="$2" ; [[ -z $FILE_B ]] && exit 1
	\cp -v  "$FILE_A"  "$FILE_B"  || exit 1
}

function DoCopyIfDifferent() {
	local FILE_A="$1" ; [[ -z $FILE_A ]] && exit 1
	local FILE_B="$2" ; [[ -z $FILE_B ]] && exit 1
	local FILENAME_A=$( \basename "$FILE_A" ) ; [[ -z $FILENAME_A ]] && exit 1
	local FILENAME_B=$( \basename "$FILE_B" ) ; [[ -z $FILENAME_B ]] && exit 1
	local DIR_A=$(      \dirname  "$FILE_A" ) ; [[ -z $DIR_A      ]] && exit 1
	local DIR_B=$(      \dirname  "$FILE_B" ) ; [[ -z $DIR_B      ]] && exit 1
	# source file not found
	if [[ ! -f "$FILE_A" ]]; then
		echo "Source file not found: $FILE_A"
		exit 1
	fi
	# dest file found
	if [[ -f "$FILE_B" ]]; then
		# temp files
		local TEMP_A=$( \mktemp )
		local TEMP_B=$( \mktemp )
		if [[ -z $TEMP_A   ]] \
		|| [[ ! -f $TEMP_A ]]; then
			failure "Failed to create a temp file for: $FILENAME_A"
			failure ; exit 1
		fi
		if [[ -z $TEMP_B   ]] \
		|| [[ ! -f $TEMP_B ]]; then
			failure "Failed to create a temp file for: $FILENAME_B"
			failure ; exit 1
		fi
		# compare checksums
		DoHashFile  "$DIR_A"  "$FILENAME_A"  "$TEMP_A"  || exit 1 ; local MD5_A="$HASH_RESULT"
		DoHashFile  "$DIR_B"  "$FILENAME_B"  "$TEMP_B"  || exit 1 ; local MD5_B="$HASH_RESULT"
		COUNT_TOTAL=$((COUNT_TOTAL+1))
		if [[ "$MD5_A" == "$MD5_B" ]]; then
			echo "[MATCH] $FILE_B"
		else
			COUNT_COPY=$((COUNT_COPY+1))
			echo -n "[DIFF ] "
			DoCopyFile  "$FILE_A"  "$FILE_B"  || exit 1
			echo "HashA: $MD5_A"
			echo "HashB: $MD5_B"
		fi
		\rm -f "$TEMP_A" "$TEMP_B"
	# dest file not found
	else
		COUNT_TOTAL=$((COUNT_TOTAL+1))
		COUNT_COPY=$((COUNT_COPY+1))
		echo -n "[COPY ] "
		DoCopyFile "$FILE_A" "$FILE_B"
	fi
	return 0
}

function DoHashFile() {
	local DIR_SRC="$1"
	local FILE_SRC="$2"
	local FILE_DST="$3"
	\pushd  "$DIR_SRC"  >/dev/null || exit 1
		local IS_HEAD=$YES
		cat  "$FILE_SRC" | while read LINE || [[ -n $LINE ]]; do
			if [[ $IS_HEAD -eq $YES ]]; then
				case "$LINE" in
				"")     ;;
				"//"*)  ;;
				"/\*"*) ;;
				"#"*)   ;;
				*) IS_HEAD=$NO ;;
				esac
			fi
			if [[ $IS_HEAD -eq $NO ]]; then
				echo  "$LINE"  >>"$FILE_DST"
			fi
		done
	\popd >/dev/null
	HASH_RESULT=$( \md5sum  "$FILE_DST" )
	local RESULT=$?
	[[ $RESULT -ne 0   ]] && return $RESULT
	[[ -z $HASH_RESULT ]] && return 1
	if [[ "$HASH_RESULT" == *" "* ]]; then
		HASH_RESULT="${HASH_RESULT%% *}"
	fi
	return 0
}



DoCopyServerFiles  "Paper"   "paper"
#TODO
#DoCopyServerFiles  "Spigot"  "spigot"
#DoCopyServerFiles  "Fabric"  "fabric"
echo



echo "Finished!"
echo -n "Found [ $COUNT_TOTAL ] file" ; [[ $COUNT_TOTAL -eq 1 ]] || echo -n "s" ; echo
echo -n "Copied [ $COUNT_COPY ] file" ; [[ $COUNT_COPY  -eq 1 ]] || echo -n "s" ; echo
echo
