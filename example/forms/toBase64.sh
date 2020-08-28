for file in *.pdf; do
  echo -n "Base64ing $file... "
  base64 -w0 "$file" > "$file.base64"
  echo "done"
done
