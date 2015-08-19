package com.letv.cdn.manager.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;     //SpringӦ�������Ļ���



    /**
     * ʵ��ApplicationContextAware�ӿڵĻص����������������Ļ���
     *
     * @param applicationContext
     * @throws org.springframework.beans.BeansException
     *
     */
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtil.applicationContext = applicationContext;
    }



    /**
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }



    /**
     * ��ȡ����
     *
     * @param name
     * @return Object һ�����������ע���bean��ʵ��
     * @throws org.springframework.beans.BeansException
     *
     */
    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }



    /**
     * ��ȡ����ΪrequiredType�Ķ���
     * ���bean���ܱ�����ת������Ӧ���쳣���ᱻ�׳���BeanNotOfRequiredTypeException��
     *
     * @param name         beanע����
     * @param requiredType ���ض�������
     * @return Object ����requiredType���Ͷ���
     * @throws org.springframework.beans.BeansException
     *
     */
    @SuppressWarnings("unchecked")
    public static Object getBean(String name, Class requiredType) throws BeansException {
        return applicationContext.getBean(name, requiredType);
    }



    /**
     * ���BeanFactory��һ����������ƥ���bean���壬�򷵻�true
     *
     * @param name
     * @return boolean
     */
    public static boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }



    /**
     * �ж��Ը�����ע���bean������һ��singleton����һ��prototype��
     * ������������Ӧ��bean����û�б��ҵ��������׳�һ���쳣��NoSuchBeanDefinitionException��
     *
     * @param name
     * @return boolean
     * @throws org.springframework.beans.factory.NoSuchBeanDefinitionException
     *
     */
    public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return applicationContext.isSingleton(name);
    }



    /**
     * @param name
     * @return Class ע����������
     * @throws org.springframework.beans.factory.NoSuchBeanDefinitionException
     *
     */
    @SuppressWarnings("unchecked")
    public static Class getType(String name) throws NoSuchBeanDefinitionException {
        return applicationContext.getType(name);
    }



    /**
     * �����bean������bean�������б����򷵻���Щ����
     *
     * @param name
     * @return
     * @throws org.springframework.beans.factory.NoSuchBeanDefinitionException
     *
     */
    public static String[] getAliases(String name) throws NoSuchBeanDefinitionException {
        return applicationContext.getAliases(name);
    }

}
