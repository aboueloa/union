module "aws_alb_controller" {
  source = "./modules/aws-alb-controller"

  main-region  = local.region
  env_name     = local.env
  cluster_name = "${local.env}-${local.eks_name}"

  vpc_id            = aws_vpc.main.id
  oidc_provider_arn = "arn:aws:iam::471112682474:oidc-provider/oidc.eks.eu-west-3.amazonaws.com/id/EC0B0BA74F3B56EA5BE80F57E21A9C6C"
}