perl -e '
%tbl = ();
open(F, "'"$sectionlist"'") || die "can't open ";
$_= <>;
while($_) {
	chomp;
	$mainname = $0;
	$tbl{$mainname} = $mainname;
	$_ = <>;
	while ($_ && m/^\s/) {
		chomp;
		s/^\s+//;
		$tbl{$mainname} = $0;
		$_ = <>;
	}
}
$_ = <>;
chomp;
print $tbl{$_}, "\n";
close(F);' 


------------------

name=`echo $name | perl -e ....`