FROM openjdk:11-slim

WORKDIR /mpls-garbage

ADD ./build/distributions/mpls-garbage-calendar.tar /mpls-garbage

EXPOSE 7000

     
CMD ["./mpls-garbage-calendar/bin/mpls-garbage-calendar"]
