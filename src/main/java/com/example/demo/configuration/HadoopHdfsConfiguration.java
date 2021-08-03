package com.example.demo.configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 * HDFS connection configuration.
 *
 * @author Andrew Timokhin
 */
@Data
@Configuration
@ConditionalOnProperty(prefix = HadoopHdfsConfiguration.PREFIX, name = "enabled") /* only for unit testing */
@ConfigurationProperties(HadoopHdfsConfiguration.PREFIX)
@Validated
@Slf4j
public class HadoopHdfsConfiguration {

    public static final String PREFIX = "hadoop.hdfs";

    private static final String HDFS_SITE = "hdfs-site.xml";
    private static final String CORE_SITE = "core-site.xml";
    private static final String YARN_SITE = "yarn-site.xml";

    private URI uri;

    private String user;

    private String confDir;

    @Bean
    public org.apache.hadoop.conf.Configuration config() throws FileNotFoundException {
        log.info("[INTERNAL] Kerberos disabled, using simple auth with user '{}'", user);

        System.setProperty("HADOOP_USER_NAME", user);

        org.apache.hadoop.conf.Configuration config = new org.apache.hadoop.conf.Configuration();

        addHdfsResource(config, confDir, CORE_SITE);
        addHdfsResource(config, confDir, HDFS_SITE);
        addHdfsResource(config, confDir, YARN_SITE);

        config.set("hadoop.security.authentication", "simple");

        UserGroupInformation.setConfiguration(config);

        log.info("[INTERNAL] Client Hadoop configuration created: Kerberos disabled");

        return config;
    }

    @Bean
    public FileSystem fileSystem(org.apache.hadoop.conf.Configuration config) throws IOException {
        return FileSystem.get(config);
    }

    private void addHdfsResource(org.apache.hadoop.conf.Configuration config, String resourceDir, String resourceName) throws FileNotFoundException {
        File resourceFile = new File(resourceDir, resourceName);

        if (resourceFile.exists() || Files.isSymbolicLink(resourceFile.toPath())) {
            config.addResource(new Path(resourceDir, resourceName));
        } else {
            throw new FileNotFoundException("file " + resourceName + " not found in " + resourceDir);
        }
    }
}



