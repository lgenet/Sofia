for file in ./*.zip
do
	echo "Starting...."
	folder=$(echo "$file" | cut -d'.' -f 2)
	echo "$folder";
	mkdir "./submissions/$folder";
	echo "Made dir..."
	
	mv "$file" "./submissions/$folder";
	cd "./submissions/$folder";
	unzip "$file";
	rm "$file";
	cd ../../;
done;
