package top.fzqblog.ant.sample;

import org.junit.Test;
import top.fzqblog.ant.pipeline.AnjukePipeline;
import top.fzqblog.ant.queue.AntQueue;
import top.fzqblog.ant.queue.TaskQueue;
import top.fzqblog.ant.task.Task;
import top.fzqblog.ant.utils.Constants;
import top.fzqblog.ant.worker.Ant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 抽离 on 2018/6/28.
 */
public class AnjukeLogin {

    public static void main(String[] args) throws Exception {
        String targetUrl = "https://vip.anjuke.com/login";
        Task task = Task.create(targetUrl);
        task.setGroup("preLogin");
        AntQueue antQueue = TaskQueue.of();
        antQueue.push(task);
        Ant ant = Ant.create().startQueue(antQueue).pipeline(new AnjukePipeline()).thread(1);
        ant.run();
    }


    @Test
    public void testIndex() throws Exception {
        String targetUrl = "https://vip.anjuke.com/user/brokerhomeV2";
        Task task = Task.create(targetUrl);
        Map<String, Object> headers = new HashMap<>();
        headers.put("cookie", "aQQ_ajkauthinfos=096lik4%2BJ1r3v7NvgoRHTZCIf6h8wHibe28%2BoaT8wrGS8SNaHw%2Bhms3n3VQIJ51Q5WsOlAwH0hz3q8V5IUAWsk83Zpyd2sc; aQQ_ajkguid=2976ABFD-CC5B-C8F0-1B64-A5173FD0FB48; aQQ_brokerauthinfos=adfbd3ZhP4aF9jYx%2Fi4F8DJzb8Pk4nn71nwUIoq4Ll3%2BydKyVb9GI110SBy3iGoClH7vQEIG64uVy1aT6%2FWf8eFJK5E5A8JO1NuVTigL5I%2B90JvZxfYXHlPAkEaIshVuuHcueTppOQn8hLwxip5hXSFDtOAadG897TEyS4IYFy8CKJDMZ6DGlJllFMl9IV%2BI6LkKeozdrwFapgWbOEcfxg6hMdhJ34gG5MS85cIPK5TbeA; aQQ_brokerguid=356C5753-37DA-7BE1-23A1-FB63A0070B77; ajk_broker_ctid=211; ajk_broker_id=3824575; ajk_broker_uid=38445483; ajk_member_id=39273832; ajk_member_key=6a03ef21977a7c7423bbf02aa402a404; ajk_member_name=1495955243_jjr282; ajk_member_time=1561905120; ctid=46; lps=http%3A%2F%2Flogin.anjuke.com%2Flogin%2Fverify%3Ftoken%3Ddb6c4642e2cbe06a5061e03f8259668e%26source%3D1%7C; lui=39273832%3A2; sessid=B4333E14-4F77-9B87-B788-121C192185DC; twe=2; ");
        task.setHeaders(headers);
        AntQueue antQueue = TaskQueue.of();
        antQueue.push(task);
        Ant ant = Ant.create().startQueue(antQueue).thread(1);
        ant.run();
    }

}
