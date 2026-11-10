terraform {
  required_version = ">= 1.5"
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 6.0"
    }
  }
}
provider "aws" {
  region = var.region
}
variable "region" {
  default = "ap-northeast-2"
}
variable "instance_type" {
  default = "t3.small"
}
data "aws_ssm_parameter" "ami" {
  name = "/aws/service/ami-amazon-linux-latest/al2023-ami-kernel-default-x86_64"
}
resource "aws_vpc" "study" {
  cidr_block           = "10.70.0.0/16"
  enable_dns_support   = true
  enable_dns_hostnames = true
  tags                 = { Name = "likelion-study" }
}
resource "aws_subnet" "public" {
  vpc_id                  = aws_vpc.study.id
  cidr_block              = "10.70.1.0/24"
  map_public_ip_on_launch = true
}
resource "aws_internet_gateway" "study" {
  vpc_id = aws_vpc.study.id
}
resource "aws_route_table" "public" {
  vpc_id = aws_vpc.study.id
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.study.id
  }
}
resource "aws_route_table_association" "public" {
  subnet_id      = aws_subnet.public.id
  route_table_id = aws_route_table.public.id
}
resource "aws_security_group" "web" {
  name   = "likelion-study-web"
  vpc_id = aws_vpc.study.id
  dynamic "ingress" {
    for_each = [80, 443]
    content {
      from_port   = ingress.value
      to_port     = ingress.value
      protocol    = "tcp"
      cidr_blocks = ["0.0.0.0/0"]
    }
  }
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}
resource "aws_iam_role" "ssm" {
  name_prefix = "likelion-ssm-"
  assume_role_policy = jsonencode({
    Version   = "2012-10-17"
    Statement = [{ Effect = "Allow", Principal = { Service = "ec2.amazonaws.com" }, Action = "sts:AssumeRole" }]
  })
}
resource "aws_iam_role_policy_attachment" "ssm" {
  role       = aws_iam_role.ssm.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonSSMManagedInstanceCore"
}
resource "aws_iam_instance_profile" "ssm" {
  name_prefix = "likelion-ssm-"
  role        = aws_iam_role.ssm.name
}
resource "aws_instance" "app" {
  ami                    = data.aws_ssm_parameter.ami.value
  instance_type          = var.instance_type
  subnet_id              = aws_subnet.public.id
  vpc_security_group_ids = [aws_security_group.web.id]
  iam_instance_profile   = aws_iam_instance_profile.ssm.name
  user_data              = file("${path.module}/user-data.sh")
  metadata_options {
    http_tokens = "required"
  }
  root_block_device {
    volume_size = 20
    encrypted   = true
  }
  tags = { Name = "likelion-surl" }
}
output "instance_id" {
  value = aws_instance.app.id
}
output "public_ip" {
  value = aws_instance.app.public_ip
}
