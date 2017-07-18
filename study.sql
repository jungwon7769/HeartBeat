select * from tab;

select friendid,color,nick,mmode from FRIEND_TABLE T0 INNER JOIN MEMBER_TABLE T1 ON T0.FRIENDID=T1.ID where T0.userid='1111' and T0.flag='1' order by T0.FRIENDID asc;

commit;
-----------------------------------------------------------------
create table msg_table(
receiver varchar2(32),
sender varchar2(32),
time varchar2(26) not null,
data varchar2(34),
flag number(2) not null
);
DELETE FROM msg_table;
select * from msg_table;
select * from msg_table where sender='히히히히' order by    time asc;
drop table msg_table purge;
insert into msg_table values('2222', 'hobin1019', '1496134046394', '0', 3);
insert into msg_table values('hobin1019', 'hjjj', '1496134046394', '0', 2);
insert into msg_table values('hobin1019', 'hjjj', '1496134046394', '1496127871132.3gp', 0);
insert into msg_table values('1111','3333','1496134046394','0',0);
insert into msg_table values('1111','2222','1494938573781','1',0);
insert into msg_table values('1111','3333','1494938573782','0',0);
insert into msg_table values('1111','4444','1494938573783','0',0);
insert into msg_table values('1111','5555','1494938573784','0',0);
DELETE FROM msg_table WHERE sender='h' and time='1494938573781';
DELETE FROM msg_table;
----------------------------------------------------------------
create table friend_table(
userid varchar2(32),
friendid varchar2(32),
flag 	varchar2(2) not null,
color varchar2(12) default 'ffff00'
);
insert into friend_table values ('1111','2222',1,'ffff00');
insert into friend_table values ('2222','1111',1,'ffff00');
insert into friend_table values ('hjjj','hobin1019',1,'ffff00');
insert into friend_table values ('hobin1019','hjjj',1,'ffff00');
insert into friend_table values ('1111','3333',1,'ffff00');
insert into friend_table values ('3333','1111',1,'ffff00');
insert into friend_table values ('1111','4444',1,'ffff00');
insert into friend_table values ('4444','1111',1,'ffff00');
insert into friend_table values ('1111','5555',1,'ffff00');
insert into friend_table values ('5555','1111',1,'ffff00');
insert into friend_table values ('2222','3333',1,'ffff00');
insert into friend_table values ('3333','2222',1,'ffff00');
insert into friend_table values ('2222','4444',1,'ffff00');
insert into friend_table values ('4444','2222',1,'ffff00');
insert into friend_table values ('2222','5555',1,'ffff00');
insert into friend_table values ('5555','2222',1,'ffff00');
insert into friend_table values ('1111','id49',1,'ffff00');
insert into friend_table values ('id49','1111',1,'ffff00');
insert into friend_table values ('히히히히','id112',1,'ffff00');
insert into friend_table values ('id112','히히히히',1,'ffff00');
select * from friend_table;
drop table friend_table purge;
select * from friend_table where userid='hobin1019' or friendid='hobin1019';
select * from friend_table where userid='1111' and friendid='2222' and flag=1;
select * from (select * from friend_table where userid='1111' and friendid='2222' and flag=1) where userid='2222' and friendid='1111' and flag=1;
UPDATE friend_table SET color = '123456' where userid='2222' or friendid='2222';
delete friend_table where (userid='h' and friendid='k' and flag='1')or(userid='k' and friendid='h' and flag='1');
delete friend_table;
------------------------------------------------------------------
create table member_table(
id varchar2(32) primary key,
pwd varchar2(32) not null,
nick varchar2(32) not null,
mmode number default 0
);
drop table member_table purge;
insert into member_table values ('2222','2222','test친구',0);

select * from member_table;
select count(*) from member_table where id='k' and pwd='k';
update member_table set pwd='1111' where id='hobin1019';
update member_table set mmode=5 where id='h';
delete from member_table where id='hi';
delete from member_table;