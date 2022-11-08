https://github.com/spring-projects/spring-graphql/issues/69

curl --location --request POST 'http://localhost:8080/graphql' \
--form 'operations="{ \"query\": \"mutation FileUpload($file: Upload!) {fileUpload(file: $file){id}}\" , \"variables\": {\"file\": null}}"' \
--form 'file=@"/Users/eopoku/Downloads/istockphoto-1298942276-612x612.jpg"' \
--form 'map="{\"file\": [\"variables.file\"]}"'
{"data":{"fileUpload":{"id":"625fd1ff-3bc7-4b89-bf8f-6608d6713a9a"}}}%  