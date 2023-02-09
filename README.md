# aws-sql-app
1. Create an RDS instance in one of the DB subnets of your VPC.
2. to include the following functions:
- download an image by name;
- show metadata for the existing images;
- upload an image;
- delete an image by name;
- get metadata for a random image;
3. After uploading some images, make some SQL queries to the RDS instance bypassing the web-application – for example, from the EC2 instances over SSH.
4. The image metadata should include last update date, name, size in bytes, and file extension.
