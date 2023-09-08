# Metrics

## Prometheus
http://localhost:9090/targets

## Grafana
http://localhost:3000

Login credentials are admin/admin  

https://grafana.com/grafana/dashboards/4701-jvm-micrometer/

## Hey

```shell
hey -c 10 -n 50 "http://localhost:8080/api/test"
hey -c 10 -n 20 "http://localhost:8080/api/books"
hey -c 2 -n 15 "http://localhost:8080/api/books?title=Clean%20code"
hey -c 10 -n 50 "http://localhost:8080/api/books?title=Fundamental%20Algorithms"
hey -c 10 -n 50 "http://localhost:8080/api/books?title=Domain%20Driven%20Design"
```
 