<?php

$url = 'https://raw.githubusercontent.com/Auburn/FastNoiseLite/master/Java/FastNoiseLite.java';
$dest = 'src/me/auburn/';



$source = \file_get_contents($url);
$source = \str_replace("\r", '', $source);
$lines = \explode("\n", $source);
$out =
	"package me.auburn;\n" .
	"// https://github.com/Auburn/FastNoiseLite\n\n";
$lineCount = 0;
foreach ($lines as $line) {
	$lineCount++;
	$tabs = 0;
	while (true) {
		$pos = $tabs * 4;
		if (\strlen($line) < $pos+4)
			break;
		if (\substr($line, $tabs*4, 4) != '    ')
			break;
		$tabs++;
	}
	$line = \substr($line, $tabs*4);
	if ($lineCount > 150) {
		$line = \str_replace('mSeed',                     'this.mSeed',                     $line);
		$line = \str_replace('mFrequency',                'this.mFrequency',                $line);
		$line = \str_replace('mNoiseType',                'this.mNoiseType',                $line);
		$line = \str_replace('mRotationType3D',           'this.mRotationType3D',           $line);
		$line = \str_replace('mTransformType3D',          'this.mTransformType3D',          $line);
		$line = \str_replace('mFractalType',              'this.mFractalType',              $line);
		$line = \str_replace('mOctaves',                  'this.mOctaves',                  $line);
		$line = \str_replace('mLacunarity',               'this.mLacunarity',               $line);
		$line = \str_replace('mGain',                     'this.mGain',                     $line);
		$line = \str_replace('mWeightedStrength',         'this.mWeightedStrength',         $line);
		$line = \str_replace('mPingPongStrength',         'this.mPingPongStrength',         $line);
		$line = \str_replace('mFractalBounding',          'this.mFractalBounding',          $line);
		$line = \str_replace('mCellularDistanceFunction', 'this.mCellularDistanceFunction', $line);
		$line = \str_replace('mCellularReturnType',       'this.mCellularReturnType',       $line);
		$line = \str_replace('mCellularJitterModifier',   'this.mCellularJitterModifier',   $line);
		$line = \str_replace('mDomainWarpType',           'this.mDomainWarpType',           $line);
		$line = \str_replace('mWarpTransformType3D',      'this.mWarpTransformType3D',      $line);
		$line = \str_replace('mDomainWarpAmp',            'this.mDomainWarpAmp',            $line);
	}
	for ($i=0; $i<$tabs; $i++)
		$out .= "\t";
	$out .= $line . "\n";
}



$out = \str_replace('GetNoise(',       'getNoise(',       $out);
$out = \str_replace('public void Set', 'public void set', $out);
$out = \str_replace('SetSeed(',        'setSeed(',        $out);
$out = \str_replace('public void DomainWarp', 'public void domainWarp', $out);

$out = \str_replace('public class FastNoiseLite', 'public class FastNoiseLiteF', $out);
$out = \str_replace('public FastNoiseLite(', 'public FastNoiseLiteF(', $out);

\file_put_contents($dest.'FastNoiseLiteF.java', $out);
echo "Created file: FastNoiseLiteF.java\n";



$out = \str_replace('public class FastNoiseLiteF', 'public class FastNoiseLiteD', $out);
$out = \str_replace('public FastNoiseLiteF(', 'public FastNoiseLiteD(', $out);

$out = \str_replace('/*FNLfloat*/ float', '/*---FNLfloat---*/ float',  $out);
$out = \str_replace('/*FNLfloat*/ float',       '/*FNLfloat*/ double', $out);
$out = \str_replace('/*---FNLfloat---*/ float', '/*FNLfloat*/ float',  $out);

\file_put_contents($dest.'FastNoiseLiteD.java', $out);
echo "Created file: FastNoiseLiteD.java\n";
