variable "common_tags" {
  type = map(any)
}

variable "product" {
  type    = string
  default = "fis"
}
variable "location" {
  default = "UK South"
}

variable "component" {
  type    = string
  default = "cos"
}

variable "env" {
  type = string
}
